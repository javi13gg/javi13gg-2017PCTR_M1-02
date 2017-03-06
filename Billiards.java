package p01;

////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
// CAMBIOS AQUÍ:
// la delcaración del array de hilos la he sacado fuera del método start, ya que, en el metodo stop
// hay que parar los hilos (de momento con un interrupt) y he implementado ese metodo


// he añadido el metodo refelct en el metodo run de la clase interna hilo

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import p01.Ball;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class Billiards extends JFrame {

	public static int Width = 800;
	public static int Height = 600;

	private JButton b_start, b_stop;

	private Board board;

	///////////////////// TODO update with number of group label. See practice statement.
	private final int N_BALL = 5;
	private Ball[] balls;
	private Thread[] hilos;

	public Billiards() {

		board = new Board();
		board.setForeground(new Color(0, 128, 0));
		board.setBackground(new Color(0, 128, 0));

		initBalls();

		b_start = new JButton("Empezar");
		b_start.addActionListener(new StartListener());
		b_stop = new JButton("Parar");
		b_stop.addActionListener(new StopListener());

		JPanel p_Botton = new JPanel();
		p_Botton.setLayout(new FlowLayout());
		p_Botton.add(b_start);
		p_Botton.add(b_stop);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(board, BorderLayout.CENTER);
		getContentPane().add(p_Botton, BorderLayout.PAGE_END);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(Width, Height);
		setLocationRelativeTo(null);
		setTitle("Practica programacion concurrente objetos moviles");
		setResizable(false);
		setVisible(true);
	}

	private void initBalls() {
		///////////////// TODO init balls ///////////////////////////////
		balls = new Ball[N_BALL];
		for (int i=0; i<N_BALL; i++){
			balls[i] = new Ball();
		}
		
	}

	private class StartListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			/////////////////// TODO Code is executed when start button is pushed
			/*
			 * hilos es un array de Thread, declarado como una variable global de la clase.
			 * Aqui lo inicializamos a un array del tipo hiloMovedor que es la clase
			 * interna en la que hemos expandido hilo y hemos implementado los metodos correspondientes
			 */
			hilos = new hiloMovedor[N_BALL];
			for (int i=0; i<N_BALL; i++){
				hilos[i] = new hiloMovedor(balls[i]);
			}
			
			for (int i=0; i<N_BALL; i++){
				hilos[i].start();
			}
			
			board.setBalls(balls);
			hiloTablero hiloX = new hiloTablero(board);
			hiloX.start();
		}
	}

	private class StopListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Code is executed when stop button is pushed
			for (int i=0; i<N_BALL; i++){
				hilos[i].interrupt();
			}

		}
	}

	public static void main(String[] args) {
		new Billiards();
	}
	
	public class hiloMovedor extends Thread{
		
		private Ball bola;
		
		public hiloMovedor(Ball bola){
			this.bola = bola;
		}
		
		@Override
		public void run(){
			bola.move();
			bola.reflect();
		}
		
	}
	
	public class hiloTablero extends Thread{
		
		private Board board;
		
		public hiloTablero(Board board){
			this.board = board;
		}
		
		@Override
		public void run(){
			board.repaint();
		}
	}
}
