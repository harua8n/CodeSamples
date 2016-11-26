## ARToolkit+libGDX example "SimpleAR" project in Android Studio

#### Versions:
Android Studio: 2.2.2
ARToolKit v5.3.1
libGDX 1.9.4

Tested on:
Android 5.x, Android 6.0

#### Links:
ARtoolkit SDK: http://artoolkit.org/download-artoolkit-sdk

ARtoolkit SDK source: https://github.com/artoolkit/artoolkit5

libGDX https://libgdx.badlogicgames.com/
#### Sample marker:

![Marker1](https://raw.githubusercontent.com/kosiara/artoolkit-android-studio-example/master/sampleMarker/patt2.jpg)

https://www.hitl.washington.edu/artoolkit/documentation/devmulti.htm

#### Sample usage:

```java
/**
 * Simple integration of ARToolKit and libGDX
 */
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
```

#### Screenshots

![Screenshot1](https://raw.githubusercontent.com/kosiara/artoolkit-android-studio-example/master/screenshots/device-2015-12-09-102231.png)
![Screenshot2](https://raw.githubusercontent.com/kosiara/artoolkit-android-studio-example/master/screenshots/device-2015-12-09-102300.png)

#### Videos

[![ARToolkit Android Studio sample](http://img.youtube.com/vi/g2z9acgPVHw/0.jpg)](https://youtu.be/g2z9acgPVHw "ARToolkit Android Studio sample")


port author: haru-a8n
