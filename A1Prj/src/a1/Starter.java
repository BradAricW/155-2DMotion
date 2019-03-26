package a1;

import java.nio.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.JButton; 

import static com.jogamp.opengl.GL4.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.GLContext;
import com.jogamp.common.nio.Buffers;

import com.jogamp.opengl.util.*;
import graphicslib3D.GLSLUtils.*;
import graphicslib3D.*;

public class Starter extends JFrame implements GLEventListener, MouseWheelListener
{	private GLCanvas myCanvas;
	private int rendering_program;
	private int vao[] = new int[1];
	private GLSLUtils util = new GLSLUtils();

	private float x, y = 0.0f;
	private float incx = 0.01f;
	private float incy = 0.01f;
	private float size = 0.0f;
	
	private static boolean vAct;
	private static boolean hAct;
	private static boolean cAct;
	private static boolean colAct;

	public Starter()
	{	setTitle("Brad Waechter - A1");
		setSize(800, 800);
		myCanvas = new GLCanvas();
		myCanvas.addGLEventListener(this);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(myCanvas);
		setVisible(true);
		FPSAnimator animator = new FPSAnimator(myCanvas, 30);
		animator.start();
		
		//instantiate booleans
		vAct = false;
		hAct = true;
		cAct = false;
		colAct = false;
		
		//create buttons
		JPanel topPanel = new JPanel();
		this.add(topPanel, BorderLayout.NORTH);	
		JButton horizButton = new JButton("Move Horizontally");
		JButton vertButton = new JButton("Move Vertically");
		JButton circButton = new JButton("Move Circularly");
		JButton colButton = new JButton("Change Color");
		topPanel.add(horizButton);
		topPanel.add(vertButton);
		topPanel.add(circButton);	
		topPanel.add(colButton);
		
		
		
		
		//create actions
		HorizAction hAction = new HorizAction();
		VertAction vAction = new VertAction();
		CircAction cAction = new CircAction();
		ColorAction colAction = new ColorAction();
		
		//associate actions
		horizButton.setAction(hAction);
		vertButton.setAction(vAction);
		circButton.setAction(cAction); 
		colButton.setAction(colAction);
		
		// get the content pane of the JFrame (this)
		JComponent contentPane = (JComponent) this.getContentPane();
		// get the "focus is in the window" input map for the content pane
		int mapName = JComponent.WHEN_IN_FOCUSED_WINDOW;
		InputMap imap = contentPane.getInputMap(mapName);
		// create a keystroke object to represent the "c" key
		KeyStroke cKey = KeyStroke.getKeyStroke('c');
		// put the "cKey" keystroke object into the content pane’s "when focus is
		// in the window" input map under the identifier name "color“
		imap.put(cKey, "color");
		// get the action map for the content pane
		ActionMap amap = contentPane.getActionMap();
		// put the "myCommand" command object into the content pane's action map
		amap.put("color", colAction);
		//have the JFrame request keyboard focus
		this.requestFocus();
		
		this.addMouseWheelListener(this); 
		
		
	}

	public void display(GLAutoDrawable drawable)
	{	GL4 gl = (GL4) GLContext.getCurrentGL();
		gl.glUseProgram(rendering_program);

		float bkg[] = { 0.0f, 0.0f, 0.0f, 1.0f };
		FloatBuffer bkgBuffer = Buffers.newDirectFloatBuffer(bkg);
		gl.glClearBufferfv(GL_COLOR, 0, bkgBuffer);

		
		if(hAct) {
			x += incx;
			if (x > 1.0f) incx = -0.01f;
			if (x < -1.0f) incx = 0.01f;
			int offset_locx = gl.glGetUniformLocation(rendering_program, "xinc");
			gl.glProgramUniform1f(rendering_program, offset_locx, x);
		}
		if(vAct) {
			y += incy;
			if (y > 1.0f) incy = -0.01f;
			if (y < -1.0f) incy = 0.01f;
			int offset_locy = gl.glGetUniformLocation(rendering_program, "yinc");
			gl.glProgramUniform1f(rendering_program, offset_locy, y);
		}
		if(cAct) {
			x +=incx;
			y += incy;
			if (x > 1.0f) incx = -0.01f;
			if (x < -1.0f) incx = 0.01f;
			if (y > 1.0f) incy = -0.01f;
			if (y < -1.0f) incy = 0.01f;
			int offset_locx = gl.glGetUniformLocation(rendering_program, "xinc");
			int offset_locy = gl.glGetUniformLocation(rendering_program, "yinc");
			gl.glProgramUniform1f(rendering_program, offset_locx, x);
			gl.glProgramUniform1f(rendering_program, offset_locy, y);
		}
		
		if(colAct) {
			gl.glProgramUniform1f(rendering_program, 0, (float) 1.0);
		}
		else {
			gl.glProgramUniform1f(rendering_program, 0, (float) 0.0); 
			
		}
		
		int sizeLoc = gl.glGetUniformLocation(rendering_program, "size");
		gl.glProgramUniform1f(rendering_program, sizeLoc, size);
		
		gl.glDrawArrays(GL_TRIANGLES,0,3);
	}

	public void init(GLAutoDrawable drawable)
	{	GL4 gl = (GL4) GLContext.getCurrentGL();
		rendering_program = createShaderProgram();
		gl.glGenVertexArrays(vao.length, vao, 0);
		gl.glBindVertexArray(vao[0]);
		System.out.println("Hello, grader!");
		System.out.println(gl.glGetString(GL_VERSION));
		Package p = Package.getPackage("com.jogamp.opengl");
		System.out.println(p.getImplementationVersion());
		System.out.println(System.getProperty("java.version"));
	}

	private int createShaderProgram()
	{	GL4 gl = (GL4) GLContext.getCurrentGL();
		int[] vertCompiled = new int[1];
		int[] fragCompiled = new int[1];
		int[] linked = new int[1];

		String vshaderSource[] = util.readShaderSource("src/a1/vert.shader");
		String fshaderSource[] = util.readShaderSource("src/a1/frag.shader");
		int lengths[];

		int vShader = gl.glCreateShader(GL_VERTEX_SHADER);
		int fShader = gl.glCreateShader(GL_FRAGMENT_SHADER);

		gl.glShaderSource(vShader, vshaderSource.length, vshaderSource, null, 0);
		gl.glShaderSource(fShader, fshaderSource.length, fshaderSource, null, 0);

		gl.glCompileShader(vShader);
		gl.glCompileShader(fShader);

		
		checkOpenGLError();  // can use returned boolean if desired
		gl.glGetShaderiv(vShader, GL_COMPILE_STATUS, vertCompiled, 0);
		if (vertCompiled[0] == 1)
		{	System.out.println("vertex compilation success");
		} else
		{	System.out.println("vertex compilation failed");
			printShaderLog(vShader);
		}

		checkOpenGLError();  // can use returned boolean if desired
		gl.glGetShaderiv(fShader, GL_COMPILE_STATUS, fragCompiled, 0);
		if (fragCompiled[0] == 1)
		{	System.out.println("fragment compilation success");
		} else
		{	System.out.println("fragment compilation failed");
			printShaderLog(fShader);
		}
		
		int vfprogram = gl.glCreateProgram();
		gl.glAttachShader(vfprogram, vShader);
		gl.glAttachShader(vfprogram, fShader);
		gl.glLinkProgram(vfprogram);
		checkOpenGLError();
		gl.glGetProgramiv(vfprogram, GL_LINK_STATUS, linked, 0);
		if (linked[0] == 1)
		{	System.out.println("linking succeeded");
		} else
		{	System.out.println("linking failed");
			printProgramLog(vfprogram);
		}
		return vfprogram;
	}

	public static void main(String[] args) { new Starter(); }
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {}
	public void dispose(GLAutoDrawable drawable) {}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(e.getWheelRotation()<0) {
			size += 0.01;
		}
		else if(e.getWheelRotation()>0) {
			size += -0.01;
		}
		
	}
	
	public static void vertSet(){
		
		cAct=false;
		hAct=false;
		vAct=true;
		
	}
	
	public static void horizSet(){
		
		cAct=false;
		hAct=true;
		vAct=false;
		
	}
	
	public static void circSet(){
		
		cAct=true;
		hAct=false;
		vAct=false;
		
	}
	
	public static void colSet() {
		if(colAct) colAct=false;
		else colAct=true;
	}
	
	
	private void printShaderLog(int shader)
	{	GL4 gl = (GL4) GLContext.getCurrentGL();
		int[] len = new int[1];
		int[] chWrittn = new int[1];
		byte[] log = null;

		// determine the length of the shader compilation log
		gl.glGetShaderiv(shader, GL_INFO_LOG_LENGTH, len, 0);
		if (len[0] > 0)
		{	log = new byte[len[0]];
			gl.glGetShaderInfoLog(shader, len[0], chWrittn, 0, log, 0);
			System.out.println("Shader Info Log: ");
			for (int i = 0; i < log.length; i++)
			{	System.out.print((char) log[i]);
			}
		}
	}

	void printProgramLog(int prog)
	{	GL4 gl = (GL4) GLContext.getCurrentGL();
		int[] len = new int[1];
		int[] chWrittn = new int[1];
		byte[] log = null;

		// determine length of the program compilation log
		gl.glGetProgramiv(prog, GL_INFO_LOG_LENGTH, len, 0);
		if (len[0] > 0)
		{	log = new byte[len[0]];
			gl.glGetProgramInfoLog(prog, len[0], chWrittn, 0, log, 0);
			System.out.println("Program Info Log: ");
			for (int i = 0; i < log.length; i++)
			{	System.out.print((char) log[i]);
			}
		}
	}

	boolean checkOpenGLError()
	{	GL4 gl = (GL4) GLContext.getCurrentGL();
		boolean foundError = false;
		GLU glu = new GLU();
		int glErr = gl.glGetError();
		while (glErr != GL_NO_ERROR)
		{	System.err.println("glError: " + glu.gluErrorString(glErr));
			foundError = true;
			glErr = gl.glGetError();
		}
		return foundError;
	}
}