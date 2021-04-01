package com.drkstrinc.pokemon.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import com.drkstrinc.pokemon.Constants;
import com.drkstrinc.pokemon.Pokemon;
import com.drkstrinc.pokemon.battle.Battle;
import com.drkstrinc.pokemon.controller.BattleController;
import com.drkstrinc.pokemon.datatype.BattleState;
import com.drkstrinc.pokemon.sound.MidiPlayer;
import com.drkstrinc.pokemon.sound.SoundEffect;
import com.drkstrinc.pokemon.ui.BattleActionBox;
import com.drkstrinc.pokemon.ui.BattleMessageBox;
import com.drkstrinc.pokemon.ui.BattleMoveBox;
import com.drkstrinc.pokemon.ui.BattleMoveInfoBox;

public class BattleScreen extends ScreenAdapter {

	private Pokemon game;

	private Battle battle;

	private BattleController battleController;

	private MidiPlayer bgm;

	private Texture background;

	private Stage uiStage;

	private Stack stack;
	private Table battleRoot;

	private BattleMessageBox messageBox;
	private BattleActionBox actionBox;
	private BattleMoveBox moveBox;
	private BattleMoveInfoBox moveInfoBox;

	private SpriteBatch batch;
	private BitmapFont font;

	public BattleScreen(Pokemon game) {
		this.game = game;
		initUI();
	}

	@Override
	public void show() {
		Gdx.app.log("BTL", "Battle Started");
		loadBGM();
		initBattle();
		setupInput();
	}

	private void setupInput() {
		battleController = new BattleController(battle, actionBox, moveBox);
		Gdx.input.setInputProcessor(battleController);
	}

	private void loadBGM() {
		bgm = new MidiPlayer("audio/bgm/WildBattle.mid");
		bgm.play();
	}

	private void initBattle() {
		battle = new Battle();
		// TODO: hange to INIT after Battle Event Queue is implemented
		battle.setState(BattleState.SELECT_ACTION);
	}

	private void initUI() {
		batch = new SpriteBatch();
		font = new BitmapFont();

		uiStage = new Stage(new ScreenViewport());
		uiStage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

		background = new Texture("image/ui/battle/unpacked/BattleBG.png");

		stack = new Stack();

		battleRoot = new Table();
		battleRoot.align(Align.bottom);
		battleRoot.setWidth(Constants.GAME_WIDTH);

		// Message Box
		messageBox = new BattleMessageBox(Pokemon.getSkin());
		messageBox.align(Align.left);
		messageBox.setVisible(false);

		Table messageTable = new Table();
		messageTable.add(messageBox).align(Align.left);
		messageTable.align(Align.left);
		stack.add(messageTable);

		// Action Box
		actionBox = new BattleActionBox(Pokemon.getSkin());
		actionBox.align(Align.right);
		actionBox.setVisible(false);

		Table actionTable = new Table();
		actionTable.add(actionBox).align(Align.bottom);
		actionTable.align(Align.right);
		stack.add(actionTable);

		// Move Box
		moveBox = new BattleMoveBox(Pokemon.getSkin());
		moveBox.align(Align.right);
		moveBox.setVisible(false);

		Table moveTable = new Table();
		moveTable.add(moveBox).align(Align.bottom);
		moveTable.align(Align.right);
		stack.add(moveTable);

		// Move Info Box (TODO: Make this display correctly)
		moveInfoBox = new BattleMoveInfoBox(Pokemon.getSkin());
		moveInfoBox.align(Align.top);
		moveInfoBox.setVisible(false);

		Table moveInfoTable = new Table();
		moveInfoTable.align(Align.top);
		moveInfoTable.add(moveInfoBox).align(Align.top);

		battleRoot.add(stack).align(Align.bottom);

		uiStage.addActor(battleRoot);
	}

	@Override
	public void render(float delta) {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.draw(battle.getPlayerActivePokemon().getBackSprite(), 0, 64);
		batch.draw(battle.getEnemyActivePokemon().getFrontSprite(),
				Gdx.graphics.getWidth() - battle.getEnemyActivePokemon().getFrontSprite().getRegionWidth(),
				Gdx.graphics.getHeight() - battle.getEnemyActivePokemon().getFrontSprite().getRegionHeight());
		batch.end();

		updateBattleUI();

		uiStage.act(delta);
		uiStage.draw();
	}

	public void updateBattleUI() {
		if (battle.getState() == BattleState.INIT) {
			startBattle();
		} else if (battle.getState() == BattleState.SELECT_ACTION) {
			startTurn();
		} else if (battle.getState() == BattleState.SELECT_MOVE) {
			chooseMove();
		} else if (battle.getState() == BattleState.SELECT_PKMN) {
			chooseNextPokemon();
		} else if (battle.getState() == BattleState.SELECT_ITEM) {
			chooseItem();
		} else if (battle.getState() == BattleState.WIN) {
			win();
		} else if (battle.getState() == BattleState.LOSE) {
			lose();
		} else if (battle.getState() == BattleState.RUN) {
			run();
		}
	}

	private void resetUI() {
		moveInfoBox.setVisible(false);
		moveBox.setVisible(false);
		actionBox.setVisible(false);
		messageBox.setVisible(false);
		battleRoot.setVisible(true);
	}

	private void startBattle() {
		battle.setState(BattleState.INIT);
		resetUI();
		messageBox.setVisible(true);
		messageBox.animateText("A wild Pokemon has appeared!");
		battle.setState(BattleState.SELECT_ACTION);
	}

	public void startTurn() {
		battle.setState(BattleState.SELECT_ACTION);
		resetUI();
		messageBox.setVisible(true);
		actionBox.setVisible(true);
	}

	public void chooseMove() {
		battle.setState(BattleState.SELECT_MOVE);
		resetUI();
		messageBox.setVisible(true);
		for (int i = 0; i <= 3; i++) {
			moveBox.setLabel(i, battle.getPlayerActivePokemon().getMoves()[i].getDisplayName());
		}
		displayPP();
		moveBox.setVisible(true);
		moveInfoBox.setVisible(true);
	}

	private void displayPP() {
		moveInfoBox.setText("PP: " + battle.getPlayerActivePokemon().getMoves()[moveBox.getSelection()].getCurrentPP());
	}

	public void chooseNextPokemon() {
		battle.setState(BattleState.SELECT_PKMN);
		resetUI();
		messageBox.setVisible(true);
		messageBox.animateText("Send out next Pokemon?");
	}

	public void chooseItem() {
		resetUI();
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
		SoundEffect.run();
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
