package com.archid;

import com.archid.lights.Directional;
import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.BatchNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import java.util.ArrayList;
import java.util.List;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.settings = new AppSettings(true);
        //app.settings.setFrameRate(30);
        //app.settings.setFrequency(30);
        app.start();
    }

    private final Deferred d = new Deferred(this);
    private final BatchNode bn = new BatchNode("Bt node");

    @Override
    public void simpleInitApp() {
        rootNode.attachChild(bn);
        
        
        AmbientLight li = new AmbientLight();
        li.setColor(ColorRGBA.DarkGray);
        rootNode.addLight(li);
        
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(0.5f, 1.0f, -1f).normalizeLocal());
        dl.setColor(ColorRGBA.Blue);
        rootNode.addLight(dl);
        
        System.out.println("FPS Limit: "+settings.getFrameRate());
        
        d.addLight(new Directional(dl, "Dir li"));
        viewPort.addProcessor(d);
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    boolean boop = true, reshaped = false;

    @Override
    public void reshape(int w, int h) {
        super.reshape(w, h);
        reshaped = true;
    }
    
    @Override
    public void simpleRender(RenderManager rm) {
        if(d.isInitialized() && boop) {
            //Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
            Material mat = new Material(assetManager, "Shaders/GBuffer.j3md");
            mat.setTexture("LightBuffer", d.getLight());
            mat.setColor("Diffuse", ColorRGBA.Red);

            Sphere b = new Sphere(16, 16, 0.5f);
            Geometry geom = new Geometry("Box", b);
            geom.move(new Vector3f(-5f, -5f, 0f));
            geom.setMaterial(mat);

            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    Geometry g = geom.clone();
                    g.move(i, j, 0);
                    bn.attachChild(g);
                }
            }
            bn.batch();
            boop = false;
        }
        else if(reshaped) {
            ArrayList<Material> mats = sceneMaterials(rootNode);
            for (Material material : mats) {
                material.setTexture("LightBuffer", d.getLight());
            }
            mats.clear();
        }
    }
    
    public ArrayList<Material> sceneMaterials(Node node) {
        ArrayList<Material> mats = new ArrayList<Material>();
        
        List<Spatial> childs = node.getChildren();
        
        for (Spatial spatial : childs) {
            if(spatial instanceof Geometry) {
                mats.add(((Geometry)spatial).getMaterial());
            }
            else if(spatial instanceof Node) {
                mats.addAll(sceneMaterials((Node)spatial));
            }
        }
        
        return mats;
    }
}
