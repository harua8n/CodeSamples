package org.artoolkit.ar.base.rendering;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.artoolkit.ar.base.ARToolKit;

public class GDXRenderer extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	private int markerID = -1;

	public boolean configureARScene() {

		markerID = ARToolKit.getInstance().addMarker("single;Data/patt.hiro;80");
		if (markerID < 0) return false;

		return true;

	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("world.png");
	}

	@Override
	public void render () {
//		Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (ARToolKit.getInstance().queryMarkerVisible(markerID)) {

            batch.begin();
            batch.draw(img, 0, 0);
            batch.end();
        }
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
