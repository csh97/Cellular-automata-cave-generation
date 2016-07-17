import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import javax.swing.JFrame;

public class MazeGen extends JFrame{
	static Random rand = new Random();
	static int chanceAlive = 45, lootChance = 2;
	static int width = 335, height = 182;
	static int x, y, count = 0;
	static int deathLimit = 3, birthLimit = 4, numberOfSteps = 16;
	static boolean[][] cellmap = new boolean[width][height];
	static boolean[][] loot = new boolean[width][height];
	static boolean[][] visited = new boolean[width][height];
	static MazeGen frame = new MazeGen();
	static int spawnX;
	static int spawnY;
	static int currentX, currentY;
	boolean spawn = false, lootPlaced = false;
	
	public static void main(String[] args){
		frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		frame.setSize(1366,768);
		frame.setVisible(true);
		
		startMap();
		randSpawn();
		frame.repaint();
	}
	
	public static void startMap(){
		cellmap = initialiseMap(cellmap);
		
		for(int i=0; i<numberOfSteps; i++){
	        cellmap = SimStep(cellmap);
	    }
		
		loot = placeLoot(loot);
		
		for(int x=0; x<width; x++){
			for(int y=0; y<height; y++){
				if(x==width - 1 || x==0 || y==height - 1 || y==0){
					cellmap[x][y] = true;
				}	
			}
		}
		WriteToFile(cellmap, loot);
	}
	
	public static boolean[][] initialiseMap(boolean[][] map){
		int randomChance;
		for(int x=0; x<width; x++){
			for(int y=0; y<height; y++){
				randomChance = rand.nextInt(100)+1;
				if(randomChance < chanceAlive){
					map[x][y] = true;
				}
			}
		}
		return map;
	}
	
	public static int countNeighbours(boolean[][] map, int x, int y){
		int count = 0;
		for(int i=-1; i<2;i++){
			for(int j=-1; j<2;j++){
				int neighbour_x = x+i;
				int neighbour_y = y+j;
				if(i==0 && j==0){
					
				}
				else if(neighbour_x < 0 || neighbour_y < 0 || neighbour_x >= map.length || neighbour_y >= map[0].length){
	                count = count + 1;
				}
				else if(map[neighbour_x][neighbour_y]){
					count = count + 1;
				}
			}
		}
		return count;
	}
	
	public static boolean[][] SimStep(boolean[][] oldMap){
		boolean[][] newMap = new boolean[width][height];
		for(int x=0;x<oldMap.length;x++){
			for(int y=0;y<oldMap[0].length;y++){
				int nbs = countNeighbours(oldMap,x,y);
				if(oldMap[x][y]){
					if(nbs < deathLimit){
						newMap[x][y] = true;
					}
					else{
						newMap[x][y] = false;
					}
				}
				else{
					if(nbs > birthLimit){
						newMap[x][y] = false;
					}
					else{
						newMap[x][y] = true;
					}
				}
			}
		}
		return newMap;
	}
	
	public static void randSpawn(){
		boolean spawn = false;
		do{
			spawnX = rand.nextInt(width-1) ;
			spawnY = rand.nextInt(height-1) ;
			if(cellmap[spawnX][spawnY] == false){
				spawn = true;
			}
			currentX = spawnX;
			currentY = spawnY;
		}while(!spawn);
	}
	
	public static boolean[][] placeLoot(boolean[][] map){
		int randomChance;
		for(int x=0; x<width; x++){
			for(int y=0; y<height; y++){
				randomChance = rand.nextInt(1000)+1;
				if(randomChance < lootChance && cellmap[x][y] == false){
					map[x][y] = true;
				}
			}
		}
		return map;
	}
	
	public void paint(Graphics g) {
		int blockW = 4, blockH = 4;
		for(int x=0; x<width; x++){
			for(int y=0; y<height; y++){
				if(cellmap[x][y] == true){
					g.setColor(Color.black);
					g.fillRect((blockW*(x+4)) - blockW, (blockH*(y+8)) - blockH, blockW, blockH);
				}
				if(cellmap[x][y] == false){
					g.setColor(Color.darkGray);
					g.fillRect((blockW*(x+4)) - blockW, (blockH*(y+8)) - blockH, blockW, blockH);
				}
				if(loot[x][y] == true){
					g.setColor(Color.yellow);
					g.fillRect((blockW*(x+4)) - blockW, (blockH*(y+8)) - blockH, blockW, blockH);
				}
				g.setColor(Color.red);
				g.fillRect((blockW*(spawnX+4)) - blockW, (blockH*(spawnY+8)) - blockH, blockW, blockH);
			}
		}			
	}
	
	public static void WriteToFile(boolean[][] map, boolean[][] loot){
		int[][] intMap = new int[width][height];
		for(int x=0; x<width; x++){
			for(int y=0; y<height; y++){
				if(map[x][y] == true){
					intMap[x][y] = 1;
				}
				else if(map[x][y] == false){
					intMap[x][y] = 0;
				}
			}
		}
		for(int x=0; x<width; x++){
			for(int y=0; y<height; y++){
				if(loot[x][y] == true){
					intMap[x][y] = 2;
				}
			}
		}
		try{
			File mapFile = new File("S:/Documents/JavaWorkspace/mapFile.txt");
			if(!mapFile.exists()){
				mapFile.createNewFile();
			}
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(mapFile));
			for(int x=0; x<width; x++){
				for(int y=0; y<height; y++){
					bw.write(intMap[x][y]+",");
				}
				bw.newLine();
			}	
			bw.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
