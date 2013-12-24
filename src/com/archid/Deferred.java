/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.archid;

import com.archid.lights.ArchidLight;
import com.archid.lights.Directional;
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.post.SceneProcessor;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vlad
 */
public class Deferred implements SceneProcessor {
    private final SimpleApplication app;
    private final Node lightNode = new Node("Light Node");

    private Renderer renderer;
    private ViewPort viewPort, lightViewPort;
    private RenderManager renderManager;

    private FrameBuffer Geometry, Light;
    private Texture2D depth, normals, light;
    private Material GBufferMaterial;
    private Picture quad;

    private boolean isInitialized;

    private ArrayList<ArchidLight<?>> lightQueue = new ArrayList<ArchidLight<?>>();
    private final Vector2f screenSize = new Vector2f();
    
    public Deferred(SimpleApplication app) {
        this.app = app;
    }
    
    public void initialize(RenderManager rm, ViewPort vp) {
        this.renderer = rm.getRenderer();
        this.renderManager = rm;
        this.viewPort = vp;
        
        Camera cam = vp.getCamera();
        int w = cam.getWidth();
        int h = cam.getHeight();
        
        screenSize.set(w, h);
        reshape(vp, w, h);
        
        lightNode.setCullHint(Spatial.CullHint.Never);

        lightViewPort = new ViewPort("Lights VP", cam);
        lightViewPort.attachScene(lightNode);
        lightViewPort.setOutputFrameBuffer(Light);

        Material mat = new Material(app.getAssetManager(), "Shaders/Lights/Debug/Resolve.j3md");
        mat.setTexture("LightBuffer", light);
        mat.setTexture("NormalBuffer", normals);
        
        quad = new Picture("FS Quad");
        quad.setMaterial(mat);
        
        System.out.println("Set up!");
        for(ArchidLight<?> l : lightQueue) {
            if(!l.isIsInitialized())
                l.initialize(this, app.getAssetManager());
            
            lightNode.attachChild(l);
        }
        GBufferMaterial = new Material(app.getAssetManager(), "Shaders/GBuffer/GBuffer.j3md");
        lightQueue.clear();
        
        isInitialized = true;
    }

    public void reshape(ViewPort vp, int w, int h) {

        screenSize.set(w, h);
        
        depth = new Texture2D(w, h, Image.Format.Depth);
        normals = new Texture2D(w, h, Image.Format.RGBA8);
        light = new Texture2D(w, h, Image.Format.RGB8);
        
        Geometry = new FrameBuffer(w, h, 1);
        Geometry.setDepthTexture(depth);
        Geometry.setColorTexture(normals);
        
        Light = new FrameBuffer(w, h, 1);
        Light.setColorTexture(light);
        Light.setDepthTexture(depth);
        
        List<Spatial> childs = lightNode.getChildren();
        
        for (Spatial spatial : childs) {
            if(spatial instanceof ArchidLight) {
                ArchidLight<?> al = (ArchidLight<?>)spatial;
                al.clean();
                
                al.initialize(this, app.getAssetManager());
            }
        }
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public void preFrame(float tpf) {
        lightNode.updateLogicalState(tpf);
        lightNode.updateGeometricState();
    }

    public void postQueue(RenderQueue rq) {
        renderer.setFrameBuffer(Geometry);
        renderer.clearBuffers(true, true, true);

        renderManager.setForcedMaterial(GBufferMaterial);
        renderManager.renderViewPortQueues(viewPort, false);
        renderManager.setForcedMaterial(null);
        
        renderer.setFrameBuffer(Light);
        renderer.clearBuffers(true, false, false);

        // render lights
        renderManager.renderScene(lightNode, lightViewPort);
        renderManager.renderViewPortQueues(lightViewPort, true);
        
        renderer.setFrameBuffer(null);
        renderer.clearBuffers(true, false, false);
    }

    public void postFrame(FrameBuffer out) {
        //renderManager.renderGeometry(quad);
    }

    public void cleanup() {
    }

    public ViewPort getViewPort() {
        return viewPort;
    }
    
    public Vector2f getScreenSize() {
        return screenSize;
    }

    public SimpleApplication getApp() {
        return app;
    }

    public Texture2D getDepth() {
        return depth;
    }

    public Texture2D getLight() {
        return light;
    }

    public Texture2D getNormals() {
        return normals;
    }

    public void addLight(ArchidLight light) {
        if(isInitialized) {
            if(!light.isIsInitialized()) {
                light.initialize(this, app.getAssetManager());
            }
            lightNode.attachChild(light);
        }
        else 
            lightQueue.add(light);
    }
    
}
