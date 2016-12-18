(* Exercice 1 *)

(*1*)
type weight =
  | Kilos of float
  | Livres of float
  | Carats of float;;


  (*2*)
let conversion arg =
  match arg with
  | Kilos k -> k
  | Livres l -> l /. 2.205
  | Carats c -> c /. 5000.;;

  (*3*)
type value = 
  | Int of int 
  | Float of float;;
  
let rec sum_of_values a b = match (a,b) with
  | (Int i,Int j) -> Int(i+j)
  | (Int i,Float f) -> Float((float i)+.f)
  | (Float f,Float g) -> Float(f+.g)
(*correction*)| (Float f,Int i) -> Float(f+.(float i));;
