package com.drkstrinc.pokemon.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import com.drkstrinc.pokemon.Pokemon;
import com.drkstrinc.pokemon.battle.Battle;
import com.drkstrinc.pokemon.controller.BattleController;
import com.drkstrinc.pokemon.datatype.BattleState;
import com.drkstrinc.pokemon.sound.MidiPlayer;
import com.drkstrinc.pokemon.ui.ChoiceBox;
import com.drkstrinc.pokemon.ui.MessageBox;
import com.drkstrinc.pokemon.ui.MoveSelectBox;

public class BattleScreen extends ScreenAdapter {

	private Pokemon game;

	private Battle battle;

	private BattleController battleController;

	private MidiPlayer bgm;

	private Texture background;

	private Stage uiStage;
	private Table messageRoot;
	private MessageBox messageBox;
	private ChoiceBox choiceBox;
	private Table moveSelectRoot;
	private MoveSelectBox moveSelectBox;

	private SpriteBatch batch;
	private BitmapFont font;

	public BattleScreen(Pokemon game) {
		this.game = game;

		initUI();
	}

	@Override
	public void show() {
		Gdx.app.log("BTL", "Battle Started");
		setupInput();
		loadBGM();
		initBattle();
	}

	private void setupInput() {
		Gdx.input.setInputProcessor(battleController);
	}

	private void initUI() {
		batch = new SpriteBatch();
		font = new BitmapFont();

		uiStage = new Stage(new ScreenViewport());
		uiStage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

		background = new Texture("image/ui/battle/BattleBG.png");

		// Move Box
		moveSelectRoot = new Table();
		moveSelectRoot.setFillParent(true);
		uiStage.addActor(moveSelectRoot);

		moveSelectBox = new MoveSelectBox(Pokemon.getSkin());
		moveSelectBox.setVisible(false);

		moveSelectRoot.add(moveSelectBox).expand().align(Align.bottomLeft);

		// Choice Box
		messageRoot = new Table();
		messageRoot.setFillParent(true);
		uiStage.addActor(messageRoot);

		choiceBox = new ChoiceBox(Pokemon.getSkin());
		choiceBox.addOption("Fight");
		choiceBox.addOption("Pokemon");
		choiceBox.addOption("Item");
		choiceBox.addOption("Run");
		choiceBox.setVisible(false);

		// Message Box
		messageBox = new MessageBox(Pokemon.getSkin());
		messageBox.setVisible(false);

		Table dialogTable = new Table();
		dialogTable.add(choiceBox).expand().align(Align.bottomRight).space(1f).row();
		dialogTable.add(messageBox).expand().align(Align.bottom).space(1f);

		messageRoot.add(dialogTable).expand().align(Align.bottom);
	}

	private void loadBGM() {
		bgm = new MidiPlayer("audio/bgm/WildBattle.mid");
		bgm.play();
	}

	private void initBattle() {
		battle = new Battle();
		messageBox.animateText("Choose an action");
	}

	public void updateBattleUI() {
		if (battle.getState() == BattleState.SELECT_ACTION) {
			startTurn();
		} else if (battle.getState() == BattleState.SELECT_MOVE) {
			chooseMove();
		} else if (battle.getState() == BattleState.CHOOSE_NEXT) {
			chooseNextPokemon();
		} else if (battle.getState() == BattleState.CHOOSE_ITEM) {
			chooseItem();
		} else if (battle.getState() == BattleState.WIN) {
			win();
		} else if (battle.getState() == BattleState.LOSE) {
			lose();
		} else if (battle.getState() == BattleState.RUN) {
			run();
		}
	}

	@Override
	public void render(float delta) {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();

		updateBattleUI();

		uiStage.act(delta);
		uiStage.draw();
	}

	public void chooseNextPokemon() {
		battle.setState(BattleState.CHOOSE_NEXT);
		messageBox.setVisible(true);
		messageBox.animateText("Send out next Pokemon?");
	}

	public void chooseItem() {

	}

	public void chooseMove() {
		battle.setState(BattleState.SELECT_MOVE);
		messageBox.setVisible(true);
		for (int i = 0; i <= 3; i++) {
			String label = "------";
			// TODO: Load current Player Pokemon moves here
			moveSelectBox.setLabel(i, label.toUpperCase());
		}
		moveSelectBox.setVisible(true);
	}

	public void startTurn() {
		battle.setState(BattleState.SELECT_ACTION);
		messageBox.setVisible(true);
		choiceBox.setVisible(true);
	}

	private void win() {
		battle.win();
		game.setScreen(Pokemon.getGameScreen());
	}

	private void lose() {
		battle.lose();
		game.setScreen(Pokemon.getGameScreen());
	}

	private void run() {
		battle.run();
		game.setScreen(Pokemon.getGameScreen());
	}

	@Override
	public void dispose() {
		font.dispose();
		batch.dispose();
	}

	@Override
	public void hide() {
		Gdx.app.log("BTL", "Battle Ended");
		Gdx.input.setInputProcessor(null);
		bgm.stop();
		this.dispose();
	}
}
