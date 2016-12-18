(*Exercice 4*)
(*1*)
type 'a queue = Q of 'a list * 'a list;;    
 
let q_fun  (Q (l1, l2)) = l1@l2;;

  let q_insert x (Q(l1,l2)) = Q([x]@l1,l2);;   

    (*2*)

  let q_transfer (Q(l1,l2)) =
    match l2 with
    | [] -> Q(l2,List.rev l1)
    | t::q -> Q(l1,l2);;

    (*3*)
  let rec q_pop (Q(l1,l2)) =
    match (l1,l2) with
    | [],[] -> failwith "Q empty"
    | t::q,[] -> q_pop (q_transfer (Q(l1,l2)))
    | [],t::q -> Q(l1,q)
    | t::q,h::b -> Q(l1,b);;
    
    (*test*)

  let q =Q([],[]);;
  let q = q_insert 14 q;;
  let q = q_insert 1 q;;
  let q = q_insert 10 q;;
  let q = q_pop q;;
    
