varying vec3 normal;
varying vec3 vertex_to_light_vector;

uniform vec3 lightPos;
in vec3 ex_color;
 
void main(void)
{
   	vec3 L = normalize(lightPos - vertex_to_light_vector);   
   	vec3 E = normalize(-vertex_to_light_vector); // we are in Eye Coordinates, so EyePos is (0,0,0)  
   	vec3 R = normalize(-reflect(L,normal));  
 
   	//calculate Ambient Term:  
   	gl_FragColor = ex_color;    

   	/*calculate Diffuse Term:  
   	vec4 Idiff = ex_color * max(dot(normal,L), 0.0);
   	Idiff = clamp(Idiff, 0.0, 1.0);     
   
   	// calculate Specular Term:
   	vec4 Ispec = ex_color
                * pow(max(dot(R,E),0.0),0.3*gl_FrontMaterial.shininess);
   	Ispec = clamp(Ispec, 0.0, 1.0); 
   	// write Total Color:  
   	gl_FragColor = gl_FrontLightModelProduct.sceneColor + Iamb + Idiff + Ispec;  */   
}