// Global uniforms from JME
uniform mat4 g_ViewProjectionMatrixInverse;
uniform mat4 g_ViewMatrix;
uniform vec2 g_Resolution;
uniform vec2 g_FrustumNearFar;

uniform sampler2D m_NormalBuffer;
uniform sampler2D m_DepthBuffer;
uniform vec3 m_FrustumCorner;
uniform vec3 m_Direction;
uniform vec4 m_Color;

/*
vec3 getPosition(vec2 uv){

  //Reconstruction from depth
  float depthv = texture2D(m_DepthBuffer, uv).r;
  float depth= (2.0 * g_FrustumNearFar.x) / (g_FrustumNearFar.y + g_FrustumNearFar.x - depthv * (g_FrustumNearFar.y-g_FrustumNearFar.x));

  //one frustum corner method
  float x = mix(-m_FrustumCorner.x, m_FrustumCorner.x, uv.x);
  float y = mix(-m_FrustumCorner.y, m_FrustumCorner.y, uv.y);

  return depth* vec3(x, y, m_FrustumCorner.z);
}
*/

void main() {
    vec2 texCoord = gl_FragCoord.xy/g_Resolution;
    
    vec4 normal = texture2D(m_NormalBuffer, texCoord) * 2.0 - 1.0;
    
    vec3 dn = normalize(m_Direction);
    vec3 dir = (g_ViewMatrix * vec4(dn, 0.0)).xyz;

    float NdotL = clamp(dot(normal.xyz, -dir), 0.0, 1.0);

    vec4 diffuse = vec4(m_Color * NdotL);
    vec4 color = diffuse;

    gl_FragColor = pow(color, vec4(2.2));
}