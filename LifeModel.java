import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.Timer;

public class LifeModel implements ActionListener {

	/*
	 * This is the Model component.
	 */

	private static int SIZE = 60;
	private LifeCell[][] grid;

	LifeView myView;
	Timer timer;

	/** Construct a new model using a particular file */
	public LifeModel(LifeView view, String fileName) throws IOException {
		int r, c;
		grid = new LifeCell[SIZE][SIZE];
		for (r = 0; r < SIZE; r++)
			for (c = 0; c < SIZE; c++)
				grid[r][c] = new LifeCell();

		if (fileName == null) // use random population
		{
			for (r = 0; r < SIZE; r++) {
				for (c = 0; c < SIZE; c++) {
					if (Math.random() > 0.85) // 15% chance of a cell starting alive
						grid[r][c].setAliveNow(true);
				}
			}
		} else {
			Scanner input = new Scanner(new File(fileName));
			int numInitialCells = input.nextInt();
			for (int count = 0; count < numInitialCells; count++) {
				r = input.nextInt();
				c = input.nextInt();
				grid[r][c].setAliveNow(true);
			}
			input.close();
		}

		myView = view;
		myView.updateView(grid);

	}

	/** Constructor a randomized model */
	public LifeModel(LifeView view) throws IOException {
		this(view, null);
	}

	/** pause the simulation (the pause button in the GUI */
	public void pause() {
		timer.stop();
	}

	/** resume the simulation (the pause button in the GUI */
	public void resume() {
		timer.restart();
	}

	/** run the simulation (the pause button in the GUI */
	public void run() {
		timer = new Timer(50, this);
		timer.setCoalesce(true);
		timer.start();
	}

	/** called each time timer fires */
	public void actionPerformed(ActionEvent e) {
		oneGeneration();
		myView.updateView(grid);
	}

	/** main logic method for updating the state of the grid / simulation */
	private void oneGeneration() {

		int count = 0;
		for (int r = 0; r < SIZE; r++) {
			for (int c = 0; c < SIZE; c++) {
				if (grid[r][c].isAliveNow()) { 
					if (checkNeighbor(r, c) == 2 || checkNeighbor(r, c) == 3) { //stay alive
						grid[r][c].setAliveNext(true);
					} else {
						grid[r][c].setAliveNext(false); //become dead
					}
				} else if (!grid[r][c].isAliveNow()) {
					if (checkNeighbor(r, c) == 3) { //reborn
						grid[r][c].setAliveNow(true);
					}
				}
			}
		}
		for (int r = 0; r < SIZE; r++) {
			for (int c = 0; c < SIZE; c++) {
//				if(grid[r][c].isAliveNext())
//				{
				  grid[r][c].setAliveNow(setAliveNext());
//				}
			}
		}
		
		//iterate grid, set alivenow to alivenext (that you just computed)
	}

	private int checkNeighbor(int r, int c) {
		int count = 0;
		for (int i = r - 1; i <= r + 1; i++) {
			for (int j = -1; j <= 1; i++) {
				if (i > 0 && i < SIZE && j > 0 && j < SIZE) {
					if (i != 0 || j != 0) {
						if (grid[i][j].isAliveNow()) {
							count++;
						}
					}
				}

			}
		}
		return count;
	}
}
