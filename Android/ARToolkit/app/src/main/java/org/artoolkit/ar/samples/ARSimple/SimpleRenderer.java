/*
 *  SimpleRenderer.java
 *  ARToolKit5
 *
 *  Disclaimer: IMPORTANT:  This Daqri software is supplied to you by Daqri
 *  LLC ("Daqri") in consideration of your agreement to the following
 *  terms, and your use, installation, modification or redistribution of
 *  this Daqri software constitutes acceptance of these terms.  If you do
 *  not agree with these terms, please do not use, install, modify or
 *  redistribute this Daqri software.
 *
 *  In consideration of your agreement to abide by the following terms, and
 *  subject to these terms, Daqri grants you a personal, non-exclusive
 *  license, under Daqri's copyrights in this original Daqri software (the
 *  "Daqri Software"), to use, reproduce, modify and redistribute the Daqri
 *  Software, with or without modifications, in source and/or binary forms;
 *  provided that if you redistribute the Daqri Software in its entirety and
 *  without modifications, you must retain this notice and the following
 *  text and disclaimers in all such redistributions of the Daqri Software.
 *  Neither the name, trademarks, service marks or logos of Daqri LLC may
 *  be used to endorse or promote products derived from the Daqri Software
 *  without specific prior written permission from Daqri.  Except as
 *  expressly stated in this notice, no other rights or licenses, express or
 *  implied, are granted by Daqri herein, including but not limited to any
 *  patent rights that may be infringed by your derivative works or by other
 *  works in which the Daqri Software may be incorporated.
 *
 *  The Daqri Software is provided by Daqri on an "AS IS" basis.  DAQRI
 *  MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION
 *  THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS
 *  FOR A PARTICULAR PURPOSE, REGARDING THE DAQRI SOFTWARE OR ITS USE AND
 *  OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.
 *
 *  IN NO EVENT SHALL DAQRI BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL
 *  OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION,
 *  MODIFICATION AND/OR DISTRIBUTION OF THE DAQRI SOFTWARE, HOWEVER CAUSED
 *  AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE),
 *  STRICT LIABILITY OR OTHERWISE, EVEN IF DAQRI HAS BEEN ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 *
 *  Copyright 2015 Daqri, LLC.
 *  Copyright 2011-2015 ARToolworks, Inc.
 *
 *  Author(s): Julian Looser, Philip Lamb
 *
 */

package org.artoolkit.ar.samples.ARSimple;

import android.content.Context;
import android.opengl.GLES10;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplicationBase;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.surfaceview.ResolutionStrategy;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.artoolkit.ar.base.ARToolKit;
import org.artoolkit.ar.base.rendering.ARRenderer;
import org.artoolkit.ar.base.rendering.Cube;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * A very simple Renderer that adds a marker and draws a cube on it.
 */
public class SimpleRenderer extends ARRenderer {

	private int markerID = -1;

	private Cube cube = new Cube(40.0f, 0.0f, 0.0f, 20.0f);
	private float angle = 0.0f;
	private boolean spinning = false;
	private GLText glText;                             // A GLText Instance
    private Context context;                           // Context (from Activity)
    private int width = 100;                           // Updated to the Current Width + Height in onSurfaceChanged()
    private int height = 100;
    private int update_cnt = 0;
    com.badlogic.gdx.graphics.g2d.SpriteBatch batch;
    Texture img;

    public SimpleRenderer(Context context, AndroidApplicationBase application, AndroidApplicationConfiguration config, ResolutionStrategy resolutionStrategy) {
        super(application, config, resolutionStrategy);
        this.context = context;                         // Save Specified Context
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl,config);
//        batch = new SpriteBatch();
//        img = new Texture("world.png");

        GLES10.glClearColor(0.0f, 0.0f, 0.0f, 0.f);

        // Create the GLText
        glText = new GLText( gl, context.getAssets() );

        // Load the font from file (set size + padding), creates the texture
        // NOTE: after a successful call to this the font is ready for rendering!
        glText.load( "Roboto-Regular.ttf", 14, 2, 2 );  // Create Font (Height: 14 Pixels / X+Y Padding 2 Pixels)

    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport( 0, 0, width, height );

        // Setup orthographic projection
        gl.glMatrixMode( GL10.GL_PROJECTION );          // Activate Projection Matrix
        gl.glLoadIdentity();                            // Load Identity Matrix
        gl.glOrthof(                                    // Set Ortho Projection (Left,Right,Bottom,Top,Front,Back)
                0, width,
                0, height,
                1.0f, -1.0f
        );

        // Save width and height
        this.width = width;                             // Save Current Width
        this.height = height;                           // Save Current Height
    }



	@Override
	public boolean configureARScene() {

		markerID = ARToolKit.getInstance().addMarker("single;Data/patt.hiro;80");
		if (markerID < 0) return false;

		return true;

	}

	public void click() {
		spinning = !spinning;
	}

	public void draw(GL10 gl) {
        // Redraw background color
        gl.glClear( GL10.GL_COLOR_BUFFER_BIT );

        // Set to ModelView mode
        gl.glMatrixMode( GL10.GL_MODELVIEW );           // Activate Model View Matrix

        if (ARToolKit.getInstance().queryMarkerVisible(markerID)) {
            gl.glLoadIdentity();                            // Load Identity Matrix

            // enable texture + alpha blending
            // NOTE: this is required for text rendering! we could incorporate it into
            // the GLText class, but then it would be called multiple times (which impacts performance).
            gl.glEnable(GL10.GL_TEXTURE_2D);              // Enable Texture Mapping
            gl.glEnable(GL10.GL_BLEND);                   // Enable Alpha Blend
            gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);  // Set Alpha Blend Function

            // TEST: render the entire font texture
            gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);         // Set Color to Use
            glText.drawTexture(width, height);            // Draw the Entire Texture

            // TEST: render some strings with the font
            glText.begin(1.0f, 1.0f, 1.0f, 1.0f);         // Begin Text Rendering (Set Color WHITE)
            glText.draw("Test String : " + String.valueOf(update_cnt++), 0, 0);          // Draw Test String
            glText.draw("Line 1", 50, 50);                // Draw Test String
            glText.draw("Line 2", 100, 100);              // Draw Test String
            glText.end();                                   // End Text Rendering

            glText.begin(0.0f, 0.0f, 1.0f, 1.0f);         // Begin Text Rendering (Set Color BLUE)
            glText.draw("More Lines...", 50, 150);        // Draw Test String
            glText.draw("The End.", 50, 150 + glText.getCharHeight());  // Draw Test String
            glText.end();                                   // End Text Rendering

            // disable texture + alpha
            gl.glDisable(GL10.GL_BLEND);                  // Disable Alpha Blend
            gl.glDisable(GL10.GL_TEXTURE_2D);             // Disable Texture Mapping

        }
//		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
//
//		gl.glMatrixMode(GL10.GL_PROJECTION);
//		gl.glLoadMatrixf(ARToolKit.getInstance().getProjectionMatrix(), 0);
//
//		gl.glEnable(GL10.GL_CULL_FACE);
//		gl.glShadeModel(GL10.GL_SMOOTH);
//		gl.glEnable(GL10.GL_DEPTH_TEST);
//		gl.glFrontFace(GL10.GL_CW);
//
//		gl.glMatrixMode(GL10.GL_MODELVIEW);
//
//		if (ARToolKit.getInstance().queryMarkerVisible(markerID)) {
//
////			gl.glLoadMatrixf(ARToolKit.getInstance().queryMarkerTransformation(markerID), 0);
////
////			gl.glPushMatrix();
////			gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
////			cube.draw(gl);
////			gl.glPopMatrix();
////
////			if (spinning) angle += 5.0f;
//
//            // Redraw background color
//            gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
//
//            // Set to ModelView mode
//            gl.glMatrixMode( GL10.GL_MODELVIEW );           // Activate Model View Matrix
//            gl.glLoadIdentity();                            // Load Identity Matrix
//
//            // enable texture + alpha blending
//            // NOTE: this is required for text rendering! we could incorporate it into
//            // the GLText class, but then it would be called multiple times (which impacts performance).
//            gl.glEnable( GL10.GL_TEXTURE_2D );              // Enable Texture Mapping
//            gl.glEnable( GL10.GL_BLEND );                   // Enable Alpha Blend
//            gl.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );  // Set Alpha Blend Function
//
//            // TEST: render the entire font texture
//            gl.glColor4f( 1.0f, 1.0f, 1.0f, 1.0f );         // Set Color to Use
//            glText.drawTexture( width, height );            // Draw the Entire Texture
//
//            // TEST: render some strings with the font
//            glText.begin( 1.0f, 1.0f, 1.0f, 1.0f );         // Begin Text Rendering (Set Color WHITE)
//            glText.draw( "Test String :)", 0, 0 );          // Draw Test String
//            glText.draw( "Line 1", 50, 50 );                // Draw Test String
//            glText.draw( "Line 2", 100, 100 );              // Draw Test String
//            glText.end();                                   // End Text Rendering
//
//            glText.begin( 0.0f, 0.0f, 1.0f, 1.0f );         // Begin Text Rendering (Set Color BLUE)
//            glText.draw( "More Lines...", 50, 150 );        // Draw Test String
//            glText.draw( "The End.", 50, 150 + glText.getCharHeight() );  // Draw Test String
//            glText.end();                                   // End Text Rendering
//
//            // disable texture + alpha
//            gl.glDisable( GL10.GL_BLEND );                  // Disable Alpha Blend
//            gl.glDisable( GL10.GL_TEXTURE_2D );             // Disable Texture Mapping
//
//		}

//        batch.begin();
//        batch.draw(img, 0, 0);
//        batch.end();


	}
}