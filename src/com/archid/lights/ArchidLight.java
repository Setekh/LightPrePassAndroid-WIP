/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.archid.lights;

import com.archid.Deferred;
import com.jme3.asset.AssetManager;
import com.jme3.light.Light;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;

/**
 *
 * @author Vlad
 */
public abstract class ArchidLight<T extends Light> extends Geometry {

    protected T light;
    
    private boolean isInitialized;

    public ArchidLight(T light, String name) {
        super(name);
        this.light = light;
    }

    public void initialize(Deferred def, AssetManager assetManager) {
        setup(assetManager, def);
        isInitialized = true;
    }
    
    public void clean() {
        cleanup();
        isInitialized = false;
    }

    public T getLight() {
        return light;
    }

    public boolean isIsInitialized() {
        return isInitialized;
    }

    @Override
    public void runControlRender(RenderManager rm, ViewPort vp) {
        super.runControlRender(rm, vp);
        render(rm, vp);
    }

    @Override
    public void updateLogicalState(float tpf) {
        super.updateLogicalState(tpf);
        update(tpf);
    }

    protected abstract void setup(AssetManager assetManager, Deferred engine);
    protected abstract void render(RenderManager rm, ViewPort vp);
    protected abstract void update(float tpf);
    protected abstract void cleanup();
}
