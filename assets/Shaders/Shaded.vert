uniform mat4 g_WorldViewProjectionMatrix;
uniform mat3 g_NormalMatrix;

attribute vec3 inPosition;
attribute vec3 inNormal;
attribute vec2 inTexCoord;

varying vec2 texCoord;
varying vec2 LightUV;
varying vec3 Normal;

void main() { 
    vec4 modelSpacePos = vec4(inPosition, 1.0);
    gl_Position = g_WorldViewProjectionMatrix * modelSpacePos;
    vec4 pos = gl_Position;

    Normal = g_NormalMatrix * inNormal;

    vec2 screenPos = pos.xy / pos.w;
    LightUV = screenPos * 0.5 + 0.5;
}
