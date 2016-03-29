
package com.freedom.augmentedreality;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;

public class Renderer implements GLSurfaceView.Renderer {
 
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        ArActivity.nativeSurfaceCreated();
    }

    public void onSurfaceChanged(GL10 gl, int w, int h) {
        ArActivity.nativeSurfaceChanged(w, h);
    }

    public void onDrawFrame(GL10 gl) {
        ArActivity.nativeDrawFrame();
    }
}
