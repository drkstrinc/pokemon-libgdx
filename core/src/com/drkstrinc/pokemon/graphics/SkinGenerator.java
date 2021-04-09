package com.drkstrinc.pokemon.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * 
 * @author hydrozoa
 *
 */
public class SkinGenerator {

	private SkinGenerator() {

	}

	public static Skin generateSkin(AssetManager assetManager) {
		Skin skin = new Skin();

		if (!assetManager.isLoaded("image/ui/gs_ui.atlas")) {
			throw new GdxRuntimeException("gs_ui.atlas was not loaded");
		}

		TextureAtlas uiAtlas = assetManager.get("image/ui/gs_ui.atlas");
		TextureAtlas battleAtlas = assetManager.get("image/ui/battle/battle_ui.atlas");

		skin.add("arrow", uiAtlas.findRegion("arrow"), TextureRegion.class);

		NinePatch speechBoxOne = new NinePatch(uiAtlas.findRegion("speech1"), 12, 14, 16, 12);
		skin.add("speechbox", speechBoxOne);

		NinePatch choiceBoxOne = new NinePatch(uiAtlas.findRegion("choice1"), 12, 14, 16, 12);
		skin.add("choicebox", choiceBoxOne);

		NinePatch battleinfobox = new NinePatch(uiAtlas.findRegion("speech1"), 12, 14, 16, 12);
		battleinfobox.setPadLeft((int) battleinfobox.getTopHeight());
		skin.add("battleinfobox", battleinfobox);

		skin.add("battleplayerbox", battleAtlas.findRegion("BattlePlayerBoxS"), TextureRegion.class);
		skin.add("battleenemybox", battleAtlas.findRegion("BattleEnemyBoxS"), TextureRegion.class);
		skin.add("green", battleAtlas.findRegion("green"), TextureRegion.class);
		skin.add("yellow", battleAtlas.findRegion("yellow"), TextureRegion.class);
		skin.add("red", battleAtlas.findRegion("red"), TextureRegion.class);

		FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font/rbygsc.ttf"));
		FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();
		fontParameter.size = 12;
		fontParameter.color = new Color(96f / 255f, 96f / 255f, 96f / 255f, 1f);
		fontParameter.shadowColor = new Color(208f / 255f, 208f / 255f, 200f / 255f, 1f);
		fontParameter.shadowOffsetX = 1;
		fontParameter.shadowOffsetY = 1;

		BitmapFont font = fontGenerator.generateFont(fontParameter);
		fontGenerator.dispose();
		font.getData().setLineHeight(16f);
		skin.add("font", font);

		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = skin.getFont("font");
		skin.add("default", labelStyle);

		return skin;
	}

}
