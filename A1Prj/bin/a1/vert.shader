#version 430

uniform float yinc;
uniform float xinc;
out vec4 initialColor;
uniform float colorKey;
uniform float size;

void main(void)
{ if (gl_VertexID == 0) {
	gl_Position = vec4( 0.25+xinc+size,-0.25+yinc-size, 0.0, 1.0);
  	if(colorKey == 1.0) initialColor = vec4(1.0, 0.0, 0.0, 1.0);
  	else initialColor = vec4(0.0, 0.0, 1.0, 1.0);
  }
  else if (gl_VertexID == 1) {
  	gl_Position = vec4(-0.25+xinc-size,-0.25+yinc-size, 0.0, 1.0);
  	if(colorKey == 1.0) initialColor = vec4(0.0, 1.0, 0.0, 1.0);
  	else initialColor = vec4(0.0, 0.0, 1.0, 1.0);
  }
  else {
  	gl_Position = vec4( 0.0+xinc, 0.25+yinc+size, 0.0, 1.0);
	initialColor = vec4(0.0, 0.0, 1.0, 1.0);
  }	
}