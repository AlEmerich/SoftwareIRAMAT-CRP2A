using System;
using System.Collections.Generic;
using System.Linq;

static class Delegate {
	public static IEnumerable<J> Flatten<T, J>(this IEnumerable<T> source)
		where T : IEnumerable<J> {
		foreach (T t in source)
			foreach (J j in t)
				yield return j;
	}

	public static void ForEach<T>(this IEnumerable<T> source,
		Action<T> action) {
		foreach (T element in source)
			action(element);
	}
}

class Program {
	class Vertex<T> {
		public T data { get; }
		public List<Vertex<T>> neighbors { get; }
		public Vertex(T _data) {
			data      = _data;
			neighbors = new List<Vertex<T>>();
		}
		public void AddNeighbor(Vertex<T> w) {
			if (!neighbors.Contains(w))
				neighbors.Add(w); 
		}
	};

	public static void Main() {
		Random rnd = new Random();
		List<Vertex<int>> graph = new List<Vertex<int>>();

		for (int i = 0; i < 5; i++) // Create vertices
			graph.Add(new Vertex<int>(i));

		for (int i = 0; i < 5; i++) { // Add edges
			Vertex<int> v = graph[i];
			for (int j = 0; j < 5; j++)
				v.AddNeighbor(graph[rnd.Next(5)]);
		}
		int index = rnd.Next(5); // Add some more edges
		for (int i = 0; i < 5; i++)
			graph[i].AddNeighbor(graph[index]);

		graph.ForEach( x =>
			x.neighbors.ForEach(n => Console.WriteLine("("+x.data+" - "+n.data+")")));
	}
}
