uniform sampler2D LightBuffer;

uniform vec4 m_Diffuse;

varying vec2 texCoord;
varying vec2 LightUV;
varying vec3 Normal;

const float LightBufferScaleInv = 100.0;

void main() {
    vec4 light = texture2D(LightBuffer, LightUV);
    gl_FragColor = m_Diffuse * 0.05; // DiffuseColor * ambient intensity
    gl_FragColor += light;
}

