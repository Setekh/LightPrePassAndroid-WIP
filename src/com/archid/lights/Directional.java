/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.archid.lights;

import com.archid.Deferred;
import com.jme3.asset.AssetManager;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.shape.Quad;

/**
 *
 * @author Vlad
 */
public class Directional extends ArchidLight<DirectionalLight> {
    private final ColorRGBA color = new ColorRGBA(ColorRGBA.White);
    private final Vector3f direction = new Vector3f(0, -1, 0);

    public Directional(DirectionalLight light, String name) {
        super(light, name);
        
        mesh = new Quad(1f, 1f);
        mesh.setStatic();
        color.set(light.getColor());
        direction.set(light.getDirection());

        //setQueueBucket(RenderQueue.Bucket.Opaque);
        setCullHint(CullHint.Never);
    }

    @Override
    protected void setup(AssetManager assetManager, Deferred engine) {
       
        material = new Material(assetManager, "Shaders/Lights/Directional/Directional.j3md");
        material.setTexture("NormalBuffer", engine.getNormals());
        material.setTexture("DepthBuffer", engine.getDepth());

        material.setColor("Color", color);
        material.setVector3("Direction", direction);

        ViewPort vp = engine.getViewPort();

        float farY = (vp.getCamera().getFrustumTop() / vp.getCamera().getFrustumNear()) * vp.getCamera().getFrustumFar();
        float farX = farY * ((float) engine.getScreenSize().x / (float) engine.getScreenSize().y);
        Vector3f frustumCorner = new Vector3f(farX, farY, vp.getCamera().getFrustumFar());
        material.setVector3("FrustumCorner", frustumCorner);
    }

    public void setColor(ColorRGBA color) {
        this.color.set(color);
    }

    public ColorRGBA getColor() {
        return color;
    }
    
    @Override
    protected void render(RenderManager rm, ViewPort vp) {
        color.set(light.getColor());
        direction.set(light.getDirection());
    }

    @Override
    protected void update(float tpf) {
    }

    @Override
    protected void cleanup() {
    }
    
}
