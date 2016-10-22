package mainPackage;

import interfaceObject.EditorPane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import util.Point3d;
import util.Vector3d;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * Class providing the 3D renderer using JOGL.
 * @author alan
 * @see GLEventListener
 * @see GLCanvas
 * @see ExternalVoxelizedWorld
 */
public class Scene extends GLCanvas implements GLEventListener
{
    private static final long serialVersionUID = 2529918802799400794L;
    private static final int MAX_VOXEL_RENDER = 64000;
    private static final int NB_LAYER = 3;
    private static final int NB_COORDS = 3;
    private static final int NB_VERTICES_PER_VOXEL = 8;
    private GLU glu;
    private GLUT glut;
    private MouseCameraManager mcm;
    private Camera camera;
    private VoxelizedObject Voxy;
    private float diameter;
    private float height;
    private int slices;
    private int stacks;
    private boolean isExternal;
    private int[] vbo;
    private volatile boolean vboIsUpToDate;
    int nbVoxel;
    int numberVertToDraw;
    private int nbVoxelToRenderX;
    private int nbVoxelToRenderY;
    private int nbVoxelToRenderZ;
    private int step;
    private ArrayList<Integer> listToUpdate;
    private int XYLayer;
    private int XZLayer;
    private int YZLayer;
    private boolean layerXYHasChanged;
    private boolean layerXZHasChanged;
    private boolean layerYZHasChanged;
    private boolean justLine;
    private Color probeColor;
    private boolean keepRatio;
    //private Shader shader;
    
    //shader uniform location
    //private int lightDirLoc;
    
    /**
     * Constructor. Do the linking with the mouse and GLEvent listener.
     * @param voxSample the object to render.
     * @param dim the dimension of the window.
     */
    public Scene(final VoxelizedObject voxSample, final Dimension dim) {
        this.isExternal = false;
        this.vboIsUpToDate = true;
        this.numberVertToDraw = 0;
        this.step = 1;
        this.listToUpdate = null;
        this.XYLayer = 0;
        this.XZLayer = 0;
        this.YZLayer = 0;
        this.layerXYHasChanged = false;
        this.layerXZHasChanged = false;
        this.layerYZHasChanged = false;
        this.justLine = false;
        this.probeColor = new Color(255, 0, 0);
        this.Voxy = voxSample;
        this.addGLEventListener((GLEventListener)this);
        this.addMouseListener((MouseListener)(this.mcm = new MouseCameraManager(this)));
        this.addMouseMotionListener((MouseMotionListener)this.mcm);
        this.addMouseWheelListener((MouseWheelListener)this.mcm);
        this.nbVoxelToRenderX = this.Voxy.getNbVoxelX();
        this.nbVoxelToRenderY = this.Voxy.getNbVoxelY();
        this.nbVoxelToRenderZ = this.Voxy.getNbVoxelZ();
        keepRatio = true;
    }
    
    /**
     * If justLine is true, it becomes false, and reverse. If justLine is true, the voxel is render just by the edge.
     */
    public void changeLineOrFill() {
        if (this.justLine) {
            this.justLine = false;
        }
        else {
            this.justLine = true;
        }
    }
    
    /**
     * Update the 3D render.
     */
    private void update() {
        final float sizeVoxelX = this.Voxy.getVoxelDimensionX() / 2.0f;
        final float sizeVoxelY = this.Voxy.getVoxelDimensionY() / 2.0f;
        final float sizeVoxelZ = this.Voxy.getVoxelDimensionZ() / 2.0f;
        int countInd = 0;
        while (this.nbVoxelToRenderX * this.nbVoxelToRenderY * this.nbVoxelToRenderZ >= MAX_VOXEL_RENDER) {
            ++this.step;
            this.nbVoxelToRenderX = this.Voxy.getNbVoxelX() / this.step;
            this.nbVoxelToRenderY = this.Voxy.getNbVoxelY() / this.step;
            this.nbVoxelToRenderZ = this.Voxy.getNbVoxelZ() / this.step;
        }
        final Point3d loc = new Point3d(this.nbVoxelToRenderX * this.Voxy.getVoxelDimensionX() , -this.nbVoxelToRenderY * this.Voxy.getVoxelDimensionY() , -this.nbVoxelToRenderZ * this.Voxy.getVoxelDimensionZ());
        final Point3d targ = new Point3d(-this.nbVoxelToRenderX * this.Voxy.getVoxelDimensionX() / 2.0f, -this.nbVoxelToRenderY * this.Voxy.getVoxelDimensionY() / 2.0f, this.nbVoxelToRenderZ * this.Voxy.getVoxelDimensionZ() / 2.0f);
        final Point3d rotCenter = new Point3d(-this.nbVoxelToRenderX * this.Voxy.getVoxelDimensionX() / 2.0f, -this.nbVoxelToRenderY * this.Voxy.getVoxelDimensionY() / 2.0f, this.nbVoxelToRenderZ * this.Voxy.getVoxelDimensionZ() / 2.0f);
        this.camera.setLocation(loc);
        this.camera.setTarget(targ);
        this.camera.setRotCenter(rotCenter);
        this.camera.updateQuaternion();
        this.camera.updateTranslations();
        this.nbVoxel = this.nbVoxelToRenderX * this.nbVoxelToRenderY * this.nbVoxelToRenderZ;
        this.numberVertToDraw = 0;
        final FloatBuffer arrayVertices = Buffers.newDirectFloatBuffer(NB_COORDS*NB_VERTICES_PER_VOXEL * (this.nbVoxel + NB_LAYER));
        final FloatBuffer arrayColor = Buffers.newDirectFloatBuffer(4*NB_VERTICES_PER_VOXEL * (this.nbVoxel + NB_LAYER));
        final IntBuffer arrayIndices = Buffers.newDirectIntBuffer(NB_COORDS*NB_VERTICES_PER_VOXEL * (this.nbVoxel + NB_LAYER));
        for (int k = 0; k < this.nbVoxelToRenderZ; ++k) {
            for (int j = 0; j < this.nbVoxelToRenderY; ++j) {
                for (int i = 0; i < this.nbVoxelToRenderX; ++i) {
                    final VoxelObject vox = this.Voxy.getVoxelToIndices(i * this.step, j * this.step, k * this.step);
                    if (vox != null && !vox.isEmpty() && vox.getMaterial().isActive()) {
                        arrayVertices.put(new float[] { sizeVoxelX - i * sizeVoxelX * 2.0f, sizeVoxelY - j * sizeVoxelY * 2.0f, sizeVoxelZ + k * sizeVoxelZ * 2.0f, -sizeVoxelX - i * sizeVoxelX * 2.0f, sizeVoxelY - j * sizeVoxelY * 2.0f, sizeVoxelZ + k * sizeVoxelZ * 2.0f, sizeVoxelX - i * sizeVoxelX * 2.0f, sizeVoxelY - j * sizeVoxelY * 2.0f, -sizeVoxelZ + k * sizeVoxelZ * 2.0f, -sizeVoxelX - i * sizeVoxelX * 2.0f, sizeVoxelY - j * sizeVoxelY * 2.0f, -sizeVoxelZ + k * sizeVoxelZ * 2.0f, sizeVoxelX - i * sizeVoxelX * 2.0f, -sizeVoxelY - j * sizeVoxelY * 2.0f, sizeVoxelZ + k * sizeVoxelZ * 2.0f, -sizeVoxelX - i * sizeVoxelX * 2.0f, -sizeVoxelY - j * sizeVoxelY * 2.0f, sizeVoxelZ + k * sizeVoxelZ * 2.0f, sizeVoxelX - i * sizeVoxelX * 2.0f, -sizeVoxelY - j * sizeVoxelY * 2.0f, -sizeVoxelZ + k * sizeVoxelZ * 2.0f, -sizeVoxelX - i * sizeVoxelX * 2.0f, -sizeVoxelY - j * sizeVoxelY * 2.0f, -sizeVoxelZ + k * sizeVoxelZ * 2.0f });
                        arrayIndices.put(new int[] { 0 + NB_VERTICES_PER_VOXEL * countInd, 2 + NB_VERTICES_PER_VOXEL * countInd, 3 + NB_VERTICES_PER_VOXEL * countInd, 1 + NB_VERTICES_PER_VOXEL * countInd, 4 + NB_VERTICES_PER_VOXEL * countInd, 6 + NB_VERTICES_PER_VOXEL * countInd, 2 + NB_VERTICES_PER_VOXEL * countInd, 0 + NB_VERTICES_PER_VOXEL * countInd, 5 + NB_VERTICES_PER_VOXEL * countInd, 7 + NB_VERTICES_PER_VOXEL * countInd, 6 + NB_VERTICES_PER_VOXEL * countInd, 4 + NB_VERTICES_PER_VOXEL * countInd, 1 + NB_VERTICES_PER_VOXEL * countInd, 3 + NB_VERTICES_PER_VOXEL * countInd, 7 + NB_VERTICES_PER_VOXEL * countInd, 5 + NB_VERTICES_PER_VOXEL * countInd, 2 + NB_VERTICES_PER_VOXEL * countInd, 6 + NB_VERTICES_PER_VOXEL * countInd, 7 + NB_VERTICES_PER_VOXEL * countInd, 3 + NB_VERTICES_PER_VOXEL * countInd, 4 + NB_VERTICES_PER_VOXEL * countInd, 0 + NB_VERTICES_PER_VOXEL * countInd, 1 + NB_VERTICES_PER_VOXEL * countInd, 5 + NB_VERTICES_PER_VOXEL * countInd });
                        final Material m = vox.getMaterial();
                        final float r = m.getColor().getRed() / 255.0f;
                        final float g = m.getColor().getGreen() / 255.0f;
                        final float b = m.getColor().getBlue() / 255.0f;
                        
                        arrayColor.put(new float[] { r, g, b, 1.0f, 
                        		r, g, b, 1.0f,
                        		r, g, b, 1.0f, 
                        		r, g, b, 1.0f, 
                        		r, g, b, 1.0f, 
                        		r, g, b, 1.0f, 
                        		r, g, b, 1.0f, 
                        		r, g, b, 1.0f });
                        
                        this.numberVertToDraw += NB_COORDS*NB_VERTICES_PER_VOXEL;
                        ++countInd;
                    }
                }
            }
        }
        
        arrayVertices.put(this.getXYLayerVertices());
        arrayColor.put(new float[] { 255.0f, 0.0f, 0.0f, 0.5f, 
        		255.0f, 0.0f, 0.0f, 0.5f, 
        		255.0f, 0.0f, 0.0f, 0.5f, 
        		255.0f, 0.0f, 0.0f, 0.5f, 
        		255.0f, 0.0f, 0.0f, 0.5f, 
        		255.0f, 0.0f, 0.0f, 0.5f, 
        		255.0f, 0.0f, 0.0f, 0.5f, 
        		255.0f, 0.0f, 0.0f, 0.5f });
        arrayIndices.put(new int[] { 0 + NB_VERTICES_PER_VOXEL * countInd, 2 + NB_VERTICES_PER_VOXEL * countInd, 3 + NB_VERTICES_PER_VOXEL * countInd, 1 + NB_VERTICES_PER_VOXEL * countInd, 
        		4 + NB_VERTICES_PER_VOXEL * countInd, 6 + NB_VERTICES_PER_VOXEL * countInd, 2 + NB_VERTICES_PER_VOXEL * countInd, 0 + NB_VERTICES_PER_VOXEL * countInd, 
        		5 + NB_VERTICES_PER_VOXEL * countInd, 7 + NB_VERTICES_PER_VOXEL * countInd, 6 + NB_VERTICES_PER_VOXEL * countInd, 4 + NB_VERTICES_PER_VOXEL * countInd, 
        		1 + NB_VERTICES_PER_VOXEL * countInd, 3 + NB_VERTICES_PER_VOXEL * countInd, 7 + NB_VERTICES_PER_VOXEL * countInd, 5 + NB_VERTICES_PER_VOXEL * countInd, 
        		2 + NB_VERTICES_PER_VOXEL * countInd, 6 + NB_VERTICES_PER_VOXEL * countInd, 7 + NB_VERTICES_PER_VOXEL * countInd, 3 + NB_VERTICES_PER_VOXEL * countInd, 
        		4 + NB_VERTICES_PER_VOXEL * countInd, 0 + NB_VERTICES_PER_VOXEL * countInd, 1 + NB_VERTICES_PER_VOXEL * countInd, 5 + NB_VERTICES_PER_VOXEL * countInd });
        this.numberVertToDraw += 24;
        ++countInd;
        arrayVertices.put(this.getXZLayerVertices());
        arrayColor.put(new float[] { 0.0f, 0.0f, 255.0f, 0.5f,
        		0.0f, 0.0f, 255.0f, 0.5f,
        		0.0f, 0.0f, 255.0f, 0.5f,
        		0.0f, 0.0f, 255.0f, 0.5f,
        		0.0f, 0.0f, 255.0f, 0.5f,
        		0.0f, 0.0f, 255.0f, 0.5f, 
        		0.0f, 0.0f, 255.0f, 0.5f, 
        		0.0f, 0.0f, 255.0f, 0.5f });
        arrayIndices.put(new int[] { 0 + NB_VERTICES_PER_VOXEL * countInd, 2 + NB_VERTICES_PER_VOXEL * countInd, 3 + NB_VERTICES_PER_VOXEL * countInd, 1 + NB_VERTICES_PER_VOXEL * countInd,
        		4 + NB_VERTICES_PER_VOXEL * countInd, 6 + NB_VERTICES_PER_VOXEL * countInd, 2 + NB_VERTICES_PER_VOXEL * countInd, 0 + NB_VERTICES_PER_VOXEL * countInd, 
        		5 + NB_VERTICES_PER_VOXEL * countInd, 7 + NB_VERTICES_PER_VOXEL * countInd, 6 + NB_VERTICES_PER_VOXEL * countInd, 4 + NB_VERTICES_PER_VOXEL * countInd,
        		1 + NB_VERTICES_PER_VOXEL * countInd, 3 + NB_VERTICES_PER_VOXEL * countInd, 7 + NB_VERTICES_PER_VOXEL * countInd, 5 + NB_VERTICES_PER_VOXEL * countInd,
        		2 + NB_VERTICES_PER_VOXEL * countInd, 6 + NB_VERTICES_PER_VOXEL * countInd, 7 + NB_VERTICES_PER_VOXEL * countInd, 3 + NB_VERTICES_PER_VOXEL * countInd,
        		4 + NB_VERTICES_PER_VOXEL * countInd, 0 + NB_VERTICES_PER_VOXEL * countInd, 1 + NB_VERTICES_PER_VOXEL * countInd, 5 + NB_VERTICES_PER_VOXEL * countInd });
        this.numberVertToDraw += 24;
        ++countInd;
        arrayVertices.put(this.getYZLayerVertices());
        arrayColor.put(new float[] { 0.0f, 255.0f, 0.0f, 0.5f, 0.0f, 255.0f, 0.0f, 0.5f, 0.0f, 255.0f, 0.0f, 0.5f, 0.0f, 255.0f, 0.0f, 0.5f, 0.0f, 255.0f, 0.0f, 0.5f, 0.0f, 255.0f, 0.0f, 0.5f, 0.0f, 255.0f, 0.0f, 0.5f, 0.0f, 255.0f, 0.0f, 0.5f });
        arrayIndices.put(new int[] { 0 + NB_VERTICES_PER_VOXEL * countInd, 2 + NB_VERTICES_PER_VOXEL * countInd, 3 + NB_VERTICES_PER_VOXEL * countInd, 1 + NB_VERTICES_PER_VOXEL * countInd, 4 + NB_VERTICES_PER_VOXEL * countInd, 6 + NB_VERTICES_PER_VOXEL * countInd, 2 + NB_VERTICES_PER_VOXEL * countInd, 0 + NB_VERTICES_PER_VOXEL * countInd, 5 + NB_VERTICES_PER_VOXEL * countInd, 7 + NB_VERTICES_PER_VOXEL * countInd, 6 + NB_VERTICES_PER_VOXEL * countInd, 4 + NB_VERTICES_PER_VOXEL * countInd, 1 + NB_VERTICES_PER_VOXEL * countInd, 3 + NB_VERTICES_PER_VOXEL * countInd, 7 + NB_VERTICES_PER_VOXEL * countInd, 5 + NB_VERTICES_PER_VOXEL * countInd, 2 + NB_VERTICES_PER_VOXEL * countInd, 6 + NB_VERTICES_PER_VOXEL * countInd, 7 + NB_VERTICES_PER_VOXEL * countInd, 3 + NB_VERTICES_PER_VOXEL * countInd, 4 + NB_VERTICES_PER_VOXEL * countInd, 0 + NB_VERTICES_PER_VOXEL * countInd, 1 + NB_VERTICES_PER_VOXEL * countInd, 5 + NB_VERTICES_PER_VOXEL * countInd });
        this.numberVertToDraw += NB_COORDS*NB_VERTICES_PER_VOXEL;
        ++countInd;
        arrayVertices.rewind();
        arrayIndices.rewind();
        arrayColor.rewind();
        final GL2 gl = GLContext.getCurrentGL().getGL2();
        gl.glGenBuffers(3, this.vbo = new int[3], 0);
        gl.glBindBuffer(34962, this.vbo[0]);
        gl.glBufferData(34962, (long)(arrayVertices.capacity() * 4), (Buffer)arrayVertices, 35048);
        gl.glBindBuffer(34962, 0);
        
        gl.glBindBuffer(34962, this.vbo[1]);
        gl.glBufferData(34962, (long)(arrayColor.capacity() * 4), (Buffer)arrayColor, 35048);
        gl.glBindBuffer(34962, 0);
        
        gl.glBindBuffer(34963, this.vbo[2]);
        gl.glBufferData(34963, (long)(arrayIndices.capacity() * 4), (Buffer)arrayIndices, 35048);
        gl.glBindBuffer(34963, 0);
        this.vboIsUpToDate = true;
        ((EditorPane)this.getParent().getParent().getParent()).setStepDrawVoxel(this.step);
    }
    
    /**
     * This method, before drawing the vbo, set the light of the scene, draw the orthogonal mark and the probe then draw the vbo.
     */
    public void display(final GLAutoDrawable drawable) {
    	   	
        final GL2 gl = drawable.getGL().getGL2();
        
        //shader.useShader(gl);
        this.mcm.setSensitivity(this.Voxy.getVoxelDimensionX() * this.Voxy.getNbVoxelX()
        		* this.Voxy.getVoxelDimensionY() * this.Voxy.getNbVoxelY()
        		* this.Voxy.getVoxelDimensionZ() * this.Voxy.getNbVoxelZ()
        		/ 5000.f);
        
        this.setLighting(gl);
        
        if (this.justLine) {
            gl.glPolygonMode(1029, 6913);
            gl.glPolygonMode(1028, 6913);
        }
        else {
            gl.glPolygonMode(1029, 6914);
            gl.glPolygonMode(1028, 6914);
        }
        gl.glClear(16640);
        gl.glMatrixMode(5888);
        gl.glLoadMatrixf(this.camera.getCameraMatrix());
        gl.glDepthFunc(515);
        if (!this.vboIsUpToDate) {
            this.update();
        }
        if (this.layerXYHasChanged) {
            this.updateXYLayer();
        }
        if (this.layerXZHasChanged) {
            this.updateXZLayer();
        }
        if (this.layerYZHasChanged) {
            this.updateYZLayer();
        }
        if (this.listToUpdate != null) {
            this.updateSpecifiedVoxel();
        }
        gl.glEnable(2977);
        gl.glEnable(32826);
        gl.glCullFace(1028);
        gl.glLineWidth(2.0f);
        gl.glColor3d(1.0, 1.0, 1.0);
        gl.glBegin(1);
        gl.glVertex3i(2, 2, -2);
        gl.glVertex3i(-2, 2, -2);
        gl.glVertex3i(2, 2, -2);
        gl.glVertex3i(2, -2, -2);
        gl.glVertex3i(2, 2, -2);
        gl.glVertex3i(2, 2, 2);
        gl.glEnd();
        gl.glColor3d(1.0, 0.0, 0.0);
        gl.glPushMatrix();
        gl.glTranslated(2.0, 2.0, 2.0);
        this.glut.glutSolidCone(0.5, 1.0, 10, 10);
        gl.glColor3d(0.0, 1.0, 0.0);
        gl.glTranslated(-4.0, 0.0, -4.0);
        gl.glRotated(-90.0, 0.0, 1.0, 0.0);
        this.glut.glutSolidCone(0.5, 1.0, 10, 10);
        gl.glColor3d(0.0, 0.0, 1.0);
        gl.glTranslated(0.0, -4.0, -4.0);
        gl.glRotated(90.0, 1.0, 0.0, 0.0);
        this.glut.glutSolidCone(0.5, 1.0, 10, 10);
        gl.glPopMatrix();
        gl.glCullFace(1029);
        if (this.isExternal) {
            gl.glPushMatrix();
            final ExternalVoxelizedWorld ext = (ExternalVoxelizedWorld)this.Voxy;
            if (!ext.ProbeIsUpdate()) {
                this.loadProbeInfo();
            }
            final GLUquadric quadric = this.glu.gluNewQuadric();
            this.glu.gluQuadricDrawStyle(quadric, 100011);
            gl.glColor4d((double)(this.probeColor.getRed() / 255.0f),
            		(double)(this.probeColor.getGreen() / 255.0f),
            		(double)(this.probeColor.getBlue() / 255.0f),
            		(double)(this.probeColor.getAlpha() / 255.0f));
            gl.glTranslated((double)(-ext.getProbeLocationX() * ext.getVoxelDimensionX() - ext.getVoxelDimensionX() * ((ext.getProbeOffsetX() - 50.0f) / 100.0f)),
            		(double)(-ext.getProbeLocationY() * ext.getVoxelDimensionY() - ext.getVoxelDimensionY() * ((ext.getProbeOffsetY() - 50.0f) / 100.0f)),
            		(double)(-ext.getVoxelDimensionZ()));
            this.glu.gluCylinder(quadric, (double)this.diameter, (double)this.diameter,
            		(double)(this.height + ext.getVoxelDimensionZ() * 2.0f),
            		this.slices,
            		this.stacks);
            gl.glPopMatrix();
        }
        gl.glEnableClientState(32884);
        gl.glEnableClientState(32886);
        gl.glBindBuffer(34962, this.vbo[0]);
        gl.glVertexPointer(3, 5126, 0, 0L);
        gl.glBindBuffer(34962, this.vbo[1]);
        gl.glColorPointer(4, 5126, 0, 0L);
        gl.glBindBuffer(34963, this.vbo[2]);
        gl.glEnable(3042);
        gl.glBlendFunc(770, 771);
        gl.glDrawElements(7, this.numberVertToDraw, 5125, 0L);
        gl.glDisable(3042);
        gl.glBindBuffer(34962, 0);
        gl.glDisableVertexAttribArray(0);
        
        //shader.dontUseShader(gl);
    }
    
    public void dispose(final GLAutoDrawable drawable) {
    }
    
    /**
     * Init the scene and the camera.
     * @see com.jogamp.opengl.GLEventListener#init(com.jogamp.opengl.GLAutoDrawable)
     * @see MouseCameraManager
     * @see Camera
     */
    public void init(final GLAutoDrawable drawable) {
        GLProfile.initSingleton();
        final GL2 gl = drawable.getGL().getGL2();
        this.glu = GLU.createGLU((GL)gl);
        this.glut = new GLUT();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glClearDepth(1.0);
        gl.glHint(3152, 4354);
        gl.glHint(3153, 4354);
        gl.glShadeModel(7425);
        gl.glMatrixMode(5889);
        gl.glLoadIdentity();
        gl.glMatrixMode(5888);
        gl.glLoadIdentity();
        gl.glEnable(2929);
        gl.glEnable(2884);
        gl.glEnable(32826);
        gl.glEnable(2903);
        gl.glMaterialf(1028, 5633, 100.0f);
        final Point3d loc = new Point3d(this.Voxy.getNbVoxelX() * this.Voxy.getVoxelDimensionX() / 2.0f, -this.Voxy.getNbVoxelY() * this.Voxy.getVoxelDimensionY() * 2.0f, -this.Voxy.getNbVoxelZ() * this.Voxy.getVoxelDimensionZ());
        final Point3d targ = new Point3d(-this.Voxy.getNbVoxelX() * this.Voxy.getVoxelDimensionX() / 2.0f, -this.Voxy.getNbVoxelY() * this.Voxy.getVoxelDimensionY() / 2.0f, this.Voxy.getNbVoxelZ() * this.Voxy.getVoxelDimensionZ() / 2.0f);
        final Vector3d up = new Vector3d(0.0, 0.0, -1.0);
        final Point3d rotCenter = new Point3d(-this.Voxy.getNbVoxelX() * this.Voxy.getVoxelDimensionX() / 2.0f, -this.Voxy.getNbVoxelY() * this.Voxy.getVoxelDimensionY() / 2.0f, this.Voxy.getNbVoxelZ() * this.Voxy.getVoxelDimensionZ() / 2.0f);
        if (this.camera == null) {
            this.camera = new Camera(loc, targ, up, rotCenter);
        }
        this.mcm.updateData();
        this.update();
        if (this.Voxy instanceof ExternalVoxelizedWorld) {
            this.loadProbeInfo();
            this.isExternal = true;
        }

        //SHADER
        /*shader = new Shader();
        shader.vsrc = shader.loadShader("../resources/vert.shader");
        shader.fsrc = shader.loadShader("../resources/frag.shader");
        shader.init(gl);*/
        
    }
    
    /**
     * Set the lighting of the scene.
     * @param gl the openGl context.
     */
    private void setLighting(final GL2 gl) {
        gl.glPushMatrix();
        gl.glLoadIdentity();
        final float[] lightPos = { 15.0f, 25.0f, 15.0f, 1.0f };
        gl.glEnable(2896);
        gl.glEnable(16384);
        final float[] noAmbient = { 0.8f, 0.8f, 0.8f, 1.0f };
        final float[] spec = { 0.8f, 0.8f, 0.8f, 1.0f };
        final float[] diffuse = { 0.2f, 0.2f, 0.2f, 1.0f };
        final float[] rgba = { 0.0f, 0.0f, 0.0f };
        gl.glMaterialfv(1028, 4608, rgba, 0);
        gl.glMaterialfv(1028, 4610, rgba, 0);
        gl.glMaterialf(1028, 5633, 1.0f);
        final FloatBuffer Light1Dir = Buffers.newDirectFloatBuffer(new float[] { -1.0f, -1.0f, -1.0f });
        final FloatBuffer exponent = Buffers.newDirectFloatBuffer(new float[] { 5.0f });
        gl.glLightfv(16384, 4608, noAmbient, 0);
        gl.glLightfv(16384, 4610, spec, 0);
        gl.glLightfv(16384, 4609, diffuse, 0);
        gl.glLightfv(16384, 4611, lightPos, 0);
        gl.glLighti(16384, 4614, 50);
        gl.glLightfv(16384, 4612, Light1Dir);
        gl.glLightfv(16384, 4613, exponent);
        gl.glLightf(16384, 4615, 1.0f);
        gl.glLightf(16384, 4616, 0.0f);
        gl.glLightf(16384, 4617, 0.0f);
        gl.glPopMatrix();
    }
    
    public void reshape(final GLAutoDrawable drawable, final int x, final int y, int width, int height) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glMatrixMode(5889);
        
        gl.glViewport(0, 0, width, height);
        gl.glLoadIdentity();
        
        float perspective = this.Voxy.VoxelDimensionX * this.Voxy.nbVoxelX *
        		this.Voxy.VoxelDimensionY * this.Voxy.nbVoxelY * 
        		this.Voxy.VoxelDimensionZ * this.Voxy.nbVoxelZ;
        if(keepRatio)
        	this.glu.gluPerspective(70.0, (float)( (float) width / (float) height), 0.1, perspective);
        else
        	this.glu.gluPerspective(70.0, 1, 0.1, perspective);
        
        gl.glMatrixMode(5888);
        gl.glLoadIdentity();
        
        this.repaint();
    }
    
    /**
     * This method reload the number of voxel, if the object was modified by the user.
     */
    public void setToUpdate() {
        if (!this.Voxy.isUpdateForScene) {
            this.nbVoxelToRenderX = this.Voxy.getNbVoxelX();
            this.nbVoxelToRenderY = this.Voxy.getNbVoxelY();
            this.nbVoxelToRenderZ = this.Voxy.getNbVoxelZ();
            this.step = 1;
            this.Voxy.isUpdateForScene = true;
            if(this.Voxy instanceof ExternalVoxelizedWorld)
            {
            	((ExternalVoxelizedWorld) this.Voxy).setProbeIsUpdate(false);
            }
        }
        
        this.vboIsUpToDate = false;
    }
    
    /**
     * Change the object rendered.
     * @param voxO the new VoxelizdObject to render.
     */
    public void setNewObject(final VoxelizedObject voxO) {
        this.Voxy = voxO;
        this.setToUpdate();
    }
    
    /**
     * @return the voxelized object rendered.
     */
    public VoxelizedObject getVoxelObject() {
        return this.Voxy;
    }
    
    /**
     * Set the list of voxel coordinates to reload in vbo.
     * @param list the list to set.
     */
    public void setListToUpdate(final ArrayList<Integer> list) {
        this.listToUpdate = list;
    }
    
    /**
     * Update the voxel which its indices is set in the listToUpdate.
     */
    private void updateSpecifiedVoxel() {
        final GL2 gl = GLContext.getCurrentGL().getGL2();
        for (int i = 0; i < this.listToUpdate.size(); i += 3) {
            final int index = this.listToUpdate.get(i) / this.step + this.listToUpdate.get(i + 1) / this.step * this.Voxy.getNbVoxelX() / this.step + this.listToUpdate.get(i + 2) / this.step * this.Voxy.getNbVoxelX() / this.step * this.Voxy.getNbVoxelY() / this.step;
            final VoxelObject vox = this.Voxy.getVoxelToIndices(this.listToUpdate.get(i), this.listToUpdate.get(i + 1), this.listToUpdate.get(i + 2));
            final FloatBuffer colorArray = Buffers.newDirectFloatBuffer(32);
            final Material m = vox.getMaterial();
            if (m.isActive()) {
                final float r = m.getColor().getRed() / 255.0f;
                final float g = m.getColor().getGreen() / 255.0f;
                final float b = m.getColor().getBlue() / 255.0f;
                colorArray.put(new float[] { r, g, b, 1.0f, r, g, b, 1.0f, r, g, b, 1.0f, r, g, b, 1.0f, r, g, b, 1.0f, r, g, b, 1.0f, r, g, b, 1.0f, r, g, b, 1.0f });
                colorArray.rewind();
                gl.glBindBuffer(34962, this.vbo[1]);
                gl.glBufferSubData(34962, (long)(32 * index * 4), 128L, (Buffer)colorArray);
                gl.glBindBuffer(34962, 0);
            }
            else {
                this.setToUpdate();
            }
        }
        this.listToUpdate = null;
    }
    
    /**
     * @param xYLayer the xYLayer to set
     */
    public void setXYLayer(final float xYLayer) {
        this.XYLayer = (int)xYLayer / this.step;
        this.layerXYHasChanged = true;
    }
    
    /**
     * Update the XY layer coordinates in vbo, before get it by {@link Scene#getXYLayerVertices()}.
     */
    public void updateXYLayer() {
        final GL2 gl = GLContext.getCurrentGL().getGL2();
        final FloatBuffer arrayVertices = this.getXYLayerVertices();
        gl.glBindBuffer(34962, this.vbo[0]);
        gl.glBufferSubData(34962, (long)((this.numberVertToDraw - 72) * 4), 96L, (Buffer)arrayVertices);
        gl.glBindBuffer(34962, 0);
        this.layerXYHasChanged = false;
    }
    
    /**
     * @param xZLayer the xYLayer to set.
     */
    public void setXZLayer(final float xZLayer) {
        this.XZLayer = (int)xZLayer / this.step;
        this.layerXZHasChanged = true;
    }
    
    /**
     * Update the XZ layer coordinates in vbo, before get it by {@link Scene#getXZLayerVertices()}.
     */
    public void updateXZLayer() {
        final GL2 gl = GLContext.getCurrentGL().getGL2();
        final FloatBuffer arrayVertices = this.getXZLayerVertices();
        gl.glBindBuffer(34962, this.vbo[0]);
        gl.glBufferSubData(34962, (long)((this.numberVertToDraw - 48) * 4), 96L, (Buffer)arrayVertices);
        gl.glBindBuffer(34962, 0);
        this.layerXZHasChanged = false;
    }
    
    /**
     * @param yZLayer the yZLayer to set.
     */
    public void setYZLayer(final float yZLayer) {
        this.YZLayer = (int)yZLayer / this.step;
        this.layerYZHasChanged = true;
    }
    
    /**
     * Update the YZ layer coordinates in vbo, before get it by {@link Scene#getYZLayerVertices()}
     */
    public void updateYZLayer() {
        final GL2 gl = GLContext.getCurrentGL().getGL2();
        final FloatBuffer arrayVertices = this.getYZLayerVertices();
        gl.glBindBuffer(34962, this.vbo[0]);
        gl.glBufferSubData(34962, (long)((this.numberVertToDraw - 24) * 4), 96L, (Buffer)arrayVertices);
        gl.glBindBuffer(34962, 0);
        this.layerYZHasChanged = false;
    }
    
    /**
     * @return the vertices of the XY layer when XYLayer has been changed.
     */
    public FloatBuffer getXYLayerVertices() {
        final float sizeVoxelX = this.Voxy.getVoxelDimensionX() / 2.0f;
        final float sizeVoxelY = this.Voxy.getVoxelDimensionY() / 2.0f;
        final float sizeVoxelZ = this.Voxy.getVoxelDimensionZ() / 2.0f;
        final FloatBuffer arrayVertices = Buffers.newDirectFloatBuffer(24);
        arrayVertices.put(new float[] { -(sizeVoxelX * 2.0f * this.nbVoxelToRenderX + sizeVoxelX),
        		-(this.nbVoxelToRenderY * sizeVoxelY * 2.0f + sizeVoxelY),
        		sizeVoxelZ * 1.5f + sizeVoxelZ * 2.0f * this.XYLayer,
        		-(sizeVoxelX * 2.0f * this.nbVoxelToRenderX + sizeVoxelX),
        		sizeVoxelY * 3.0f,
        		sizeVoxelZ * 1.5f + sizeVoxelZ * 2.0f * this.XYLayer,
        		-(sizeVoxelX * 2.0f * this.nbVoxelToRenderX + sizeVoxelX),
        		-(this.nbVoxelToRenderY * sizeVoxelY * 2.0f + sizeVoxelY),
        		-sizeVoxelZ * 1.5f + sizeVoxelZ * 2.0f * this.XYLayer,
        		-(sizeVoxelX * 2.0f * this.nbVoxelToRenderX + sizeVoxelX),
        		sizeVoxelY * 3.0f,
        		-sizeVoxelZ * 1.5f + sizeVoxelZ * 2.0f * this.XYLayer,
        		sizeVoxelX * 3.0f,
        		-(this.nbVoxelToRenderY * sizeVoxelY * 2.0f + sizeVoxelY),
        		sizeVoxelZ * 1.5f + sizeVoxelZ * 2.0f * this.XYLayer, sizeVoxelX * 3.0f,
        		sizeVoxelY * 3.0f,
        		sizeVoxelZ * 1.5f + sizeVoxelZ * 2.0f * this.XYLayer,
        		sizeVoxelX * 3.0f,
        		-(this.nbVoxelToRenderY * sizeVoxelY * 2.0f + sizeVoxelY),
        		-sizeVoxelZ * 1.5f + sizeVoxelZ * 2.0f * this.XYLayer, sizeVoxelX * 3.0f,
        		sizeVoxelY * 3.0f,
        		-sizeVoxelZ * 1.5f + sizeVoxelZ * 2.0f * this.XYLayer });
        arrayVertices.rewind();
        return arrayVertices;
    }
    
    /**
     * @return the vertices of the XZ layer when XZLayer has been changed.
     */
    public FloatBuffer getXZLayerVertices() {
        final float sizeVoxelX = this.Voxy.getVoxelDimensionX() / 2.0f;
        final float sizeVoxelY = this.Voxy.getVoxelDimensionY() / 2.0f;
        final float sizeVoxelZ = this.Voxy.getVoxelDimensionZ() / 2.0f;
        final FloatBuffer arrayVertices = Buffers.newDirectFloatBuffer(24);
        arrayVertices.put(new float[] { -(sizeVoxelX * 2.0f * this.nbVoxelToRenderX + sizeVoxelX),
        		-sizeVoxelY * 1.5f - sizeVoxelY * 2.0f * this.XZLayer,
        		-sizeVoxelZ * 3.0f,
        		-(sizeVoxelX * 2.0f * this.nbVoxelToRenderX + sizeVoxelX),
        		sizeVoxelY * 1.5f - sizeVoxelY * 2.0f * this.XZLayer,
        		-sizeVoxelZ * 3.0f,
        		-(sizeVoxelX * 2.0f * this.nbVoxelToRenderX + sizeVoxelX),
        		-sizeVoxelY * 1.5f - sizeVoxelY * 2.0f * this.XZLayer,
        		this.nbVoxelToRenderZ * sizeVoxelZ * 2.0f + sizeVoxelZ,
        		-(sizeVoxelX * 2.0f * this.nbVoxelToRenderX + sizeVoxelX),
        		sizeVoxelY * 1.5f - sizeVoxelY * 2.0f * this.XZLayer,
        		this.nbVoxelToRenderZ * sizeVoxelZ * 2.0f + sizeVoxelZ,
        		sizeVoxelX * 3.0f,
        		-sizeVoxelY * 1.5f - sizeVoxelY * 2.0f * this.XZLayer,
        		-sizeVoxelZ * 3.0f,
        		sizeVoxelX * 3.0f,
        		sizeVoxelY * 1.5f - sizeVoxelY * 2.0f * this.XZLayer,
        		-sizeVoxelZ * 3.0f, sizeVoxelX * 3.0f,
        		-sizeVoxelY * 1.5f - sizeVoxelY * 2.0f * this.XZLayer,
        		this.nbVoxelToRenderZ * sizeVoxelZ * 2.0f + sizeVoxelZ,
        		sizeVoxelX * 3.0f,
        		sizeVoxelY * 1.5f - sizeVoxelY * 2.0f * this.XZLayer,
        		this.nbVoxelToRenderZ * sizeVoxelZ * 2.0f + sizeVoxelZ });
        arrayVertices.rewind();
        return arrayVertices;
    }
    
    /**
     * @return the vertices of the YZ layer when YZLayer has been changed.
     */
    public FloatBuffer getYZLayerVertices() {
        final float sizeVoxelX = this.Voxy.getVoxelDimensionX() / 2.0f;
        final float sizeVoxelY = this.Voxy.getVoxelDimensionY() / 2.0f;
        final float sizeVoxelZ = this.Voxy.getVoxelDimensionZ() / 2.0f;
        final FloatBuffer arrayVertices = Buffers.newDirectFloatBuffer(24);
        arrayVertices.put(new float[] { -sizeVoxelX * 1.5f - sizeVoxelX * 2.0f * this.YZLayer,
        		-(this.nbVoxelToRenderY * sizeVoxelY * 2.0f + sizeVoxelY),
        		-sizeVoxelZ * 3.0f,
        		-sizeVoxelX * 1.5f - sizeVoxelX * 2.0f * this.YZLayer,
        		sizeVoxelY * 3.0f,
        		-sizeVoxelZ * 3.0f,
        		-sizeVoxelX * 1.5f - sizeVoxelX * 2.0f * this.YZLayer,
        		-(this.nbVoxelToRenderY * sizeVoxelY * 2.0f + sizeVoxelY),
        		sizeVoxelZ * 2.0f * this.nbVoxelToRenderZ + sizeVoxelZ,
        		-sizeVoxelX * 1.5f - sizeVoxelX * 2.0f * this.YZLayer,
        		sizeVoxelY * 3.0f,
        		sizeVoxelZ * 2.0f * this.nbVoxelToRenderZ + sizeVoxelZ,
        		sizeVoxelX * 1.5f - sizeVoxelX * 2.0f * this.YZLayer,
        		-(this.nbVoxelToRenderY * sizeVoxelY * 2.0f + sizeVoxelY),
        		-sizeVoxelZ * 3.0f,
        		sizeVoxelX * 1.5f - sizeVoxelX * 2.0f * this.YZLayer,
        		sizeVoxelY * 3.0f,
        		-sizeVoxelZ * 3.0f,
        		sizeVoxelX * 1.5f - sizeVoxelX * 2.0f * this.YZLayer,
        		-(this.nbVoxelToRenderY * sizeVoxelY * 2.0f + sizeVoxelY),
        		sizeVoxelZ * 2.0f * this.nbVoxelToRenderZ + sizeVoxelZ,
        		sizeVoxelX * 1.5f - sizeVoxelX * 2.0f * this.YZLayer,
        		sizeVoxelY * 3.0f,
        		sizeVoxelZ * 2.0f * this.nbVoxelToRenderZ + sizeVoxelZ });
        arrayVertices.rewind();
        return arrayVertices;
    }
    
    private void loadProbeInfo() {
        final ExternalVoxelizedWorld ext = (ExternalVoxelizedWorld)this.Voxy;
        float rapport = 1.0f;
        if (ext.getDimensionUnit().equals("1 \u03bcm")) {
            rapport = 1000.0f;
        }
        else if (ext.getDimensionUnit().equals("10 \u03bcm")) {
            rapport = 100.0f;
        }
        else if (ext.getDimensionUnit().equals("100 \u03bcm")) {
            rapport = 10.0f;
        }
        else if (ext.getDimensionUnit().equals("1 cm")) {
            rapport = 0.1f;
        }
        this.diameter = ext.getProbeDiameter() / 2.0f * rapport;
        this.height = ext.getNbVoxelZ() * ext.getVoxelDimensionZ();
        this.slices = 10;
        this.stacks = ext.getNumberCells();
        ext.setProbeIsUpdate(true);
    }
    
    /**
     * @return the camera.
     * @see Camera
     */
    public Camera getCamera() {
        return this.camera;
    }
    
    /**
     * Set the color of the probe.
     * @param color the new color of the probe.
     */
    public void setProbeColor(final Color color) {
        this.probeColor = color;
    }

	/**
	 * @return the keepRatio
	 */
	public boolean isKeepRatio()
	{
		return keepRatio;
	}

	/**
	 * @param keepRatio the keepRatio to set
	 */
	public void setKeepRatio(boolean keepRatio)
	{
		this.keepRatio = keepRatio;
	}
}
