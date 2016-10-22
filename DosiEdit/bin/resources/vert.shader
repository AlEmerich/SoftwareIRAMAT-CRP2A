varying vec3 normal;
varying vec3 vertex_to_light_vector;

void main(void)
{
	vertex_to_light_vector = vec3(gl_ModelViewMatrix * gl_Vertex);       
   	normal = normalize(gl_NormalMatrix * gl_Normal);
   	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}