
 
using System.Collections.Generic;
using System.Linq;
using System;

class Program {
	
  public delegate T VisitorFun<V, T>(V f);

  // Interface for the figures
  public interface IFigure {
    String GetName ();
    T Accept<T>(VisitorFun<IFigure, T> v);
  };

  // Simple Figure 
  public class SimpleFigure : IFigure {
    private String _name ;
    public SimpleFigure (String name) { this._name = name ; }
    public String GetName () { return this._name ; }
    
    public T Accept<T>(VisitorFun<IFigure, T> v){
		return v(this);
	}
    
  }

  // Composite Figure : array of simple figures
  public class CompositeFigure : IFigure {
	  
	  public CompositeFigure(List<IFigure> list) {this.list = list;}
	  
	  private List<IFigure> list;
	  
	  public String GetName(){
		String name = "";
		foreach( IFigure sf in list ){
			name+= sf.GetName() + " ";
		}
		return name;
	  }
	  
	  
	  public T Accept<T>(VisitorFun<IFigure, T> v){
		 
		  T result = v(this);
		  foreach( IFigure sf in list ){
				result = sf.Accept(v);
		  }
		  return result;
	  }
	  
	  
	  
  }
      
  public static VisitorFun<IFigure , String> MakeNameFigureVisitorFun (  ) {
	string _state = "" ;
	return fig => {if( fig is SimpleFigure){return _state += fig.GetName() + " ";}else{return _state += "\n";}};
  }
  
  

  public static void Main() 
  {
    

    
    Console.WriteLine("-- Visitor --");

    IFigure Carre = new SimpleFigure("Carre");
    IFigure Triangle = new SimpleFigure("Triangle");
    IFigure Bozo = new SimpleFigure("Trapeze");
    IFigure Clown = new SimpleFigure("Rectangle");
    
    List<IFigure> list2 = new List<IFigure>();
    list2.Add(Bozo);
    list2.Add(Clown);
    
    IFigure cf2 = new CompositeFigure(list2);
    
    List<IFigure> list = new List<IFigure>();
    list.Add(Carre);
    list.Add(Triangle);
    list.Add(cf2);
    
    
    
    
    IFigure cf = new CompositeFigure(list);
    
    
    
    Console.WriteLine(cf.Accept(MakeNameFigureVisitorFun()));
    
  }
}
 
