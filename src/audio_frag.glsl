#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

varying vec4 vertColor;
varying vec3 ecNormal;
varying vec3 lightDir;

const mat3 rgb2yiq = mat3(0.299, 0.587, 0.114, 0.595716, -0.274453, -0.321263, 0.211456, -0.522591, 0.311135);
const mat3 yiq2rgb = mat3(1.0, 0.9563, 0.6210, 1.0, -0.2721, -0.6474, 1.0, -1.1070, 1.7046);
const float hue = 80.;

void main() {  
  vec3 direction = normalize(lightDir);
  vec3 normal = normalize(ecNormal);
  float intensity = abs( dot(direction, normal));

  vec3 yColor = rgb2yiq * vertColor.rgb;

  float originalHue = atan(yColor.b, yColor.g);
  float finalHue = originalHue + hue;

  float chroma = sqrt(yColor.b*yColor.b+yColor.g*yColor.g);

  vec3 yFinalColor = vec3(yColor.r, chroma * cos(finalHue), chroma * sin(finalHue));

  gl_FragColor = vec4(intensity * 1., intensity* 1., intensity* 1., 1) * vertColor;
}