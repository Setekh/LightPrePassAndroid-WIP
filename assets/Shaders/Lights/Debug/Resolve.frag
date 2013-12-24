uniform mat4 g_ViewMatrix;
uniform vec2 g_Resolution;

uniform sampler2D m_LightBuffer, m_NormalBuffer;
uniform vec3 m_LightDirection;

void main() {

    vec3 dirNormal = normalize(m_LightDirection);
    vec3 dir = (g_ViewMatrix * vec4(dirNormal, 0.0)).xyz;

    vec2 TexCoord = gl_FragCoord.xy / g_Resolution;
    vec4 LBuffer = texture2D(m_LightBuffer, TexCoord);
    //vec4 normal = texture2D(m_NormalBuffer, TexCoord) * 2.0 - 1.0;

    //float NdotL = dot(normal.xyz, dir);

    gl_FragColor = LBuffer; //vec4(1.0, vec3(0.5)) * NdotL;  
}

