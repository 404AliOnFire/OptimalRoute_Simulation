# OptimalRoute_Simulation

🚀 **OptimalRoute_Simulation**
*Optimizing Your Journey, Stage by Stage.*

---![Screenshot 2025-04-20 000736](https://github.com/user-attachments/assets/f734dcc5-b339-4501-af25-d24e10f60f3d)


## 📌 Overview
**OptimalRoute_Simulation** is a dynamic programming–based simulation tool designed to compute the most cost-effective route between cities, considering both petrol and hotel costs. The system visualizes possible paths through multiple stages and identifies optimal and alternative solutions with graphical support.

This project is suitable for:
- 🎓 Academic learning / graduation projects
- 🧠 Algorithm practice (especially Dynamic Programming)
- 🚗 Route & trip optimization simulations

---

## 🧠 Features
- ✅ Stage-by-stage city traversal
- ⛽ Considers petrol cost + 🏨 hotel cost
- 📊 Graph visualization of cities and paths
- 📁 Input loaded from file
- 💡 Displays optimal path + alternatives
- 🧾 Dynamic programming table included in results

---
![Screenshot 2025-04-20 000759](https://github.com/user-attachments/assets/64b9b537-a78f-41e3-a50e-97378cdf6a4d)


## 📂 Folder Structure
```
OptiRouteSim/
├── README.md
├── input.txt
├── Main.java
├── Vertex.java
├── Edge.java
├── DPTablePrinter.java
├── GraphSimulator.java
└── assets/
    └── sample_graph.png
```

---

## 📥 Input Format (input.txt)
```
14
Start, End
Start, [A,22,70], [B,8,80], [C,12,80]
A, [D,8,50], [E,10,70]
B, [D, 25, 50], [E,10,70]
C, [D,13,50], [E,13,70]
D, [F,25,50], [G,30,70], [H,18,70], [I, 27,60]
E, [F,12,50], [G,10,70], [H,8,70], [I, 7,60]
F, [J,26,50], [K,13,70], [L,15,60]
G, [J,8,50], [K,10,70], [L,10,60]
H, [J,20,50], [K,10,70], [L,10,60]
L, [J,15,50], [K,10,70], [L,7,60]
J, [End,10,0]
K, [End,10,0]
L, [End,10,0]
End
```

---

## 🛠️ How to Run
1. Load input data from `input.txt`
2. Run `Main.java`
3. Observe:
   - Optimal route
   - Cost breakdown
   - Alternative paths
   - Graph simulation (in console or image)

---
![image](https://github.com/user-attachments/assets/9404e036-5f5c-4c92-8902-b8aba3d4e22b)


## 👨‍💻 Code Files
- `Main.java`: Entry point. Reads input, builds graph, triggers DP logic.
- `Vertex.java`: Defines a vertex with name, stage, and adjacents.
- `Edge.java`: Connection with petrol & hotel cost.
- `DPTablePrinter.java`: Helper to print the DP cost table.
- `GraphSimulator.java`: (Optional) Visual simulation logic or export to image.

---

## 🖼️ Sample Output (Expected)
```
Optimal Path: Start → B → E → I → J → End
Total Cost: 310
Alternative 1: Start → C → E → I → J → End, Cost: 312
```


## 📜 License
MIT – Free for use with credit.

---

## 🤝 Author
Developed by  Ali Hassoneh ✨
