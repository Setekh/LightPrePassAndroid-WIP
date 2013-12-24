varying vec2 texCoord;
varying vec3 Normal;

void main() {
    vec3 normal = Normal;
    gl_FragColor = vec4(normalize(normal)*0.5+0.5, 1.0);  
}

