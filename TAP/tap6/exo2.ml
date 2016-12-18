(*Exercice 2*)

(*1*)
type 'a bintree = 
| BinEmpty
| BinNode of 'a * 'a bintree * 'a bintree

let rec bintree_build f h x = 
  if (h<=0)
  then BinEmpty
  else let (x1,x2) = f(x) in 
          BinNode (x,
            (bintree_build f (h-1) x1),
            (bintree_build f (h-1) x2));;

let tree = bintree_build f 5 1;;
  
  (*2*)

let rec bintree_map tree f =
  match tree with
  | BinEmpty -> BinEmpty
  | BinNode(x,l1,l2) -> BinNode(f(x), bintree_map l1 f, bintree_map l2 f);; 

  (*3*)
let rec bintree_insert tree value =
  match tree with
  | BinEmpty -> BinNode(value,BinEmpty,BinEmpty)
  | BinNode(x,l1,l2) ->
     if value >= x then
         bintree_insert l2 value
       else
       bintree_insert l1 value;;


(* 5*)
type 'a tree = 
| TreeEmpty
| TreeNode of 'a * 'a tree list

let rec tree_build f h x =
  (*la fonction son permet d'affecter à chaque fils sa valeur et d'appliquer la fonction buildTree recursivement sur ce noeud  *)
  let rec son s=
    match s with
      |[] -> []
      |t::q -> (tree_build f (h-1) t) :: son q in
  
  if (h<=0)
  then TreeEmpty
  else let (tab) = f(x) in
       (* la fonction f permet de determiner le nombre arbitraire de fils, elle retourne une liste de valeurs *)
          TreeNode (x,son(tab));;


let f x = [3*x;3*x+1;3*x+2];;
(*test

tree_build f 3 7;;

*)
(*6*)

let rec tree_map tree f =
  let rec modify m=
    match m with
      |	[]->[]
      |t::q -> (tree_map t f )::modify q in
    
  match tree with
  | TreeEmpty -> TreeEmpty
  | TreeNode(x,l) -> TreeNode(f(x), modify l);; 
