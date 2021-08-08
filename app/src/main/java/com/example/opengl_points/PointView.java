package com.example.opengl_points;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class PointView extends GLSurfaceView {
    public PointView(Context context) {
        super(context);
        //設定OPENGL版本
        setEGLContextClientVersion(2);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        setRenderer(new PointRenderer());
    }
    class PointRenderer implements Renderer {

        private void checkError(String message) {
            int err = GLES20.glGetError();
            if (err != GLES20.GL_NO_ERROR) {
                throw new RuntimeException(message + " -> " + err);
            }
        }

        private int createShader(int type, String source) {
            int shader = GLES20.glCreateShader(type);
            GLES20.glShaderSource(shader, source);
            checkError("glShaderSource");
            GLES20.glCompileShader(shader);
            checkError("glCompileShader");
            return shader;
        }

        @Override
        public void onSurfaceCreated(GL10 unused, EGLConfig config) {
            String coordinate="0.5, 0.5, 0.5, 1";
            String source="" +
                    "void main() {" +
                    "   gl_Position = vec4("+coordinate+");" +
                    "   gl_PointSize = 50.0;" +
                    "}";
            //頂點著色器(控制OBJECT X,Y,Z)
//            int vertex = createShader(GLES20.GL_VERTEX_SHADER, "" +
//                    "void main() {" +
//                    "   gl_Position = vec4(0, 0, 0, 1);" +
//                    "   gl_PointSize = 100.0;" +
//                    "}");
            int vertex = createShader(GLES20.GL_VERTEX_SHADER, source);


            //片段著色器(控制顏色OUTPUT)
            int fragment = createShader(GLES20.GL_FRAGMENT_SHADER, "" +
                    "precision mediump float;" +
                    "void main() {" +
                    "   gl_FragColor = vec4(0.3, 0.3, 0.3, 1);" +
                    "}");

            //建立OpenGL ES Program
            program = GLES20.glCreateProgram();
            checkError("glCreateProgram");
            GLES20.glAttachShader(program, vertex);
            checkError("glAttachShader(vertex)");
            GLES20.glAttachShader(program, fragment);
            checkError("glAttachShader(fragment)");
            GLES20.glLinkProgram(program);
            checkError("glLinkProgram");
        }

        @Override
        public void onSurfaceChanged(GL10 unused, int width, int height) {

        }

        @Override
        public void onDrawFrame(GL10 unused) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
            GLES20.glUseProgram(program);

            //可以DRAW GL_POINTS, GL_LINE_STRIP, GL_LINE_LOOP, GL_LINES, GL_TRIANGLE_STRIP, GL_TRIANGLE_FAN, and GL_TRIANGLES 這幾種
            //first:Specifies the starting index in the enabled arrays.
            //count:Specifies the number of indices to be rendered.
            GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
        }

        private int program;

    }


}
