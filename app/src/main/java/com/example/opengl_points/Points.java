package com.example.opengl_points;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class Points {
    private  int mProgram, mPositionHandle, mColorHandle, mMVPMatrixHandle;


    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "uniform vec4 vPosition;" +
                    "void main() {" +
                    "   gl_Position = uMVPMatrix * vPosition;" +
                    "   gl_PointSize = 50.0;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "   gl_FragColor = vColor;" +
                    "}";


    // Set color with red, green, blue and alpha (opacity) values
    float color[] = { 0.5f, 0.3f, 0.6f, 1.0f };

    public Points(){
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
    }

    public void draw(float[] mvpMatrix,float px,float py,float pz) {
        float positionCoords[] = {px,py,pz,1.0f};

        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetUniformLocation(mProgram, "vPosition");

        mColorHandle = GLES20.glGetUniformLocation(mProgram,"vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        GLES20.glUniform4fv(mPositionHandle, 1, positionCoords, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        //TRANSLATION
        float[] transMatrix = new float[16];

        Matrix.setIdentityM(transMatrix,0);
        Matrix.translateM(transMatrix,0,0.1f,0.1f,0.1f);
        Matrix.multiplyMM(transMatrix,0,mvpMatrix,0,transMatrix,0);


        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, transMatrix, 0);

        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
    }

}