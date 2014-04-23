uniform mat4 modelview;
uniform mat4 transform;
uniform mat4 texMatrix;
uniform mat3 normalMatrix;

uniform sampler2D texture;

uniform vec4 lightPosition;
uniform int distortion;

attribute vec4 vertex;
attribute vec4 color;
attribute vec3 normal;
attribute vec2 texCoord;



varying vec4 vertColor;
varying vec3 ecNormal;
varying vec3 lightDir;


void main() {
    vec4 vertTexCoord = texMatrix * vec4(texCoord.yx, 1.0, 1.0);
    vec4 colorX = texture2D(texture, vertTexCoord.st);

    vec4 vertTexCoord2 = texMatrix * vec4(texCoord.xy, 1.0, 1.0);
    vec4 colorX2 = texture2D(texture, vertTexCoord.st);
    colorX =sin(colorX) + cos(colorX2);

    vec3 newPosition = vertex.xyz + normal * colorX.a*vec3(distortion, distortion, distortion);
    gl_Position = transform * vec4( newPosition, 1.0 );

    vec3 ecVertex = vec3(modelview * vertex);

    ecNormal = normalize(normalMatrix * normal);
    lightDir = normalize(lightPosition.xyz - ecVertex);
    ecNormal = normal;
    vertColor = vec4(color.rgb, 1.);
}