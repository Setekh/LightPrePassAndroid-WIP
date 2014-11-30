uniform sampler2D LightBuffer;

uniform vec4 m_Diffuse;

varying vec2 texCoord;
varying vec4 pos;
varying vec3 Normal;

void main() {
    vec2 screenPos = pos.xy / pos.w;
    screenPos = screenPos * 0.5 + 0.5;
    
    vec4 light = texture2D(LightBuffer, screenPos);
    
    gl_FragColor = m_Diffuse * 0.05; // DiffuseColor * ambient intensity
    gl_FragColor += light;
}

