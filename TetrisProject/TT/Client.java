package TT;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
//3월 11일 부터 시작


public class Client{

	static int currentij [] = {0, 5, 1, 4, 1, 5, 1, 6};
	static int futureij[] = {0, 1, 2, 3, 4, 5, 6, 7};
	static ClientFrame cfr;
	static block arrblock[];
	static int currentblocknum =2;
	static int futureblocknum =2;
	static Thread t1;
	static boolean condition1 = false;
	static boolean condition2 = false;
	
	
	public static void main(String[] args) {
	
		//1.그래픽생성

		arrblock = new block[7];
		arrblock[0] = new block1();
		arrblock[1] = new block2();
		arrblock[2] = new block3();
		arrblock[3] = new block4();
		arrblock[4] = new block5();
		arrblock[5] = new block6();
		arrblock[6] = new block7();
		
		cfr = new ClientFrame();
		cfr.jfr.addKeyListener(new keyAdap());
		cfr.jfr.addWindowListener(new Windowcontrol());
		t1 = new Thread(cfr);
		


		for (int i = 0; i <currentij.length; i+=2) {
				ClientFrame.p1[currentij[i]][currentij[i+1]].setBackground(ClientFrame.fColor(Client.currentblocknum));
				ClientFrame.p1[currentij[i]][currentij[i+1]].empty = 1;
		}
		keyAdap.changeBlock();
		t1.start();
	}
}
class ZPanel extends Panel{
	int empty ;
}

class ClientFrame extends JFrame implements Runnable{
	JFrame jfr ;
	ZPanel jp1;
	ZPanel jp2;
	ZPanel jp3;
	ZPanel jp4;
	static int MAX_V = 18;
	static int MAX_H = 12;
	static ZPanel p1[][] ;
	static ZPanel p2[][] ;
	static ZPanel p3[][] ;
	Random r ;
	static Color c;
	public ClientFrame() {
		
		r = new Random();
		p1 = new ZPanel[MAX_V][MAX_H];
		p2 = new ZPanel[4][4];
		p3 = new ZPanel[MAX_V][MAX_H];
		
		jfr = new JFrame("1");
		jfr.setSize(900, 700);
		jfr.setLayout(new GridLayout(1,3));
		
		
		jp1 = new ZPanel();
		jp2 = new ZPanel();
		jp3 = new ZPanel();
		jp4 = new ZPanel();
		
		jp1.setLayout(new GridLayout(MAX_V,MAX_H));
		jp3.setLayout(new GridLayout(MAX_V,MAX_H));

		
		for (int i = 0; i < p1.length; i++) {
			for (int j = 0; j < p1[i].length; j++) {
				p1[i][j] = new ZPanel();
				p1[i][j].setBackground(oColor());
				jp1.add(p1[i][j]);
			}
		}

		jp2.setLayout(null);
		
		
		//다음블록 생성
		jp4.setLayout(new GridLayout(4, 4));
		jp4.setLocation(100,300);
		jp4.setSize(100,100);
		
		for (int i = 0; i < p2.length; i++) {
			for (int j = 0; j < p2[i].length; j++) {
				p2[i][j] = new ZPanel();
				p2[i][j].setBackground(oColor());
				jp4.add(p2[i][j]);	
			}
		}
		jp2.add(jp4);
		
		jfr.add(jp1);
		jfr.add(jp2);
		jfr.add(jp3);
		
		//상대편 블록생성
		for (int i = 0; i < p3.length; i++) {
			for (int j = 0; j < p3[i].length; j++) {
				p3[i][j] = new ZPanel();
				p3[i][j].setBackground(rColor());
				jp3.add(p3[i][j]);
				
			}
		}

		jfr.setVisible(true);
		

	}
	public synchronized void run(){
		int currentspeedtime = 1000;
		long firsttime = System.currentTimeMillis();
		long secondtime ;
		//내리기전에 블록 컬러 바꾸기
		while(true){
			try{
				Thread.sleep(currentspeedtime);
			}catch (InterruptedException e){
				
			}
			while(Client.condition2 ==true){
				try{
					wait();
					
				}catch (InterruptedException z){
				}
			}
			Client.condition1 = true;
		
			
			for (int i = 0; i <Client.currentij.length; i+=2) {
				ClientFrame.p1[Client.currentij[i]][Client.currentij[i+1]].setBackground(ClientFrame.oColor());
				ClientFrame.p1[Client.currentij[i]][Client.currentij[i+1]].empty=0;

			}
			//아래로 내리기
			boolean cham = true;
			
			for (int i = 0; i <Client.currentij.length; i+=2) {
					if((Client.currentij[i]+1)<(ClientFrame.MAX_V)){
						//client
						if(ClientFrame.p1[Client.currentij[i]+1][Client.currentij[i+1]].empty==0){
						}else {
							for (int k = 0; k <Client.currentij.length; k+=2) {
								ClientFrame.p1[Client.currentij[k]][Client.currentij[k+1]].setBackground(ClientFrame.fColor(Client.currentblocknum));
								ClientFrame.p1[Client.currentij[k]][Client.currentij[k+1]].empty=1;
								Client.cfr.repaint();
							}
							keyAdap.changeBlock();
							cham = false;

						}
					}else{
						for (int k = 0; k <Client.currentij.length; k+=2) {
							ClientFrame.p1[Client.currentij[k]][Client.currentij[k+1]].setBackground(ClientFrame.fColor(Client.currentblocknum));
							ClientFrame.p1[Client.currentij[k]][Client.currentij[k+1]].empty=1;
							Client.cfr.repaint();
						}
						keyAdap.changeBlock();
					}
			}
			for (int i = 0; i < p1[0].length; i++) {
				if(ClientFrame.p1[0][i].empty==1){
					System.exit(0);
				}
			}

			if(cham){
				int i = 0;
				for (int k = 0; k < Client.currentij.length; k+=2) {
					if(Client.currentij[k]<ClientFrame.MAX_V -1){
						i++;
					}
				}
				if(i==(Client.currentij.length/2)){
					for (int k = 0; k < Client.currentij.length; k+=2) {
						if(Client.currentij[k]<ClientFrame.MAX_V -1){
							Client.currentij[k]++;
						}
					}
				}
			}
			
			for (int i = 0; i <Client.currentij.length; i+=2) {
					ClientFrame.p1[Client.currentij[i]][Client.currentij[i+1]].setBackground(ClientFrame.fColor(Client.currentblocknum));
					ClientFrame.p1[Client.currentij[i]][Client.currentij[i+1]].empty=1;
					Client.cfr.repaint();
			}
			

			
			secondtime = System.currentTimeMillis();
			if((secondtime-firsttime)>60000){
				firsttime = System.currentTimeMillis();
				if(currentspeedtime >50){
					currentspeedtime -= 100;
				}

			}
			
			notifyAll();
			Client.condition1 = false;

		}
	}
	static Color oColor(){
		c= new Color(255,255,255);
		return c;
	}
	static Color fColor(int a){
		if(a==0){
			c = new Color(50,50,50);
		}
		if(a==1){
			c = new Color(50,100,50);
		}
		if(a==2){
			c = new Color(100,50,50);
		}
		if(a==3){
			c = new Color(50,50,100);
		}
		if(a==4){
			c = new Color(100,150,100);
		}
		if(a==5){
			c = new Color(200,100,100);
		}
		if(a==6){
			c = new Color(150,100,150);
		}
		return c;
	}
	
	Color rColor(){
		c = new Color(r.nextInt(255)+1,r.nextInt(255)+1,r.nextInt(255)+1);
		return c;
	}
}

class keyAdap extends KeyAdapter{
	//움직이기전에 패널컬러 기본으로
	public void setBackColor(){
		for (int i = 0; i <Client.currentij.length; i+=2) {
				ClientFrame.p1[Client.currentij[i]][Client.currentij[i+1]].setBackground(ClientFrame.oColor());
				ClientFrame.p1[Client.currentij[i]][Client.currentij[i+1]].empty=0;
		}
	}
	//위
	public void pushUp(){
		if(Client.currentblocknum==0){
			Client.arrblock[0].changeBlockForm(Client.currentij);
		}
		if(Client.currentblocknum==1){
			Client.arrblock[1].changeBlockForm(Client.currentij);
		}
		if(Client.currentblocknum==2){
			Client.arrblock[2].changeBlockForm(Client.currentij);
		}
		if(Client.currentblocknum==3){
			Client.arrblock[3].changeBlockForm(Client.currentij);
		}
		if(Client.currentblocknum==4){
			Client.arrblock[4].changeBlockForm(Client.currentij);
		}
		if(Client.currentblocknum==5){
			Client.arrblock[5].changeBlockForm(Client.currentij);
		}
		if(Client.currentblocknum==6){
			Client.arrblock[6].changeBlockForm(Client.currentij);
		}
	}
	
	//아래
	public void moveDown(){
		boolean cham = true;
		
		for (int i = 0; i <Client.currentij.length; i+=2) {
				//블록의 지금 위치 +1 이 패널 최대크기보다 작은것만 다음으로 넘어간다.
				if((Client.currentij[i]+1)<(ClientFrame.MAX_V)){
					//client
					if(ClientFrame.p1[Client.currentij[i]+1][Client.currentij[i+1]].empty==0){
					}else {
						cham = false;
					}
				}
		}
		if(cham){
			int i = 0;
			for (int k = 0; k < Client.currentij.length; k+=2) {
				if(Client.currentij[k]<ClientFrame.MAX_V -1){
					i++;
				}
			}
			if(i==(Client.currentij.length/2)){
				for (int k = 0; k < Client.currentij.length; k+=2) {
					if(Client.currentij[k]<ClientFrame.MAX_V -1){
						Client.currentij[k]++;
					}
				}
			}
		}
	}
	//오른쪽
	public void moveRight(){
		boolean cham = true;
		
		for (int i = 0; i <Client.currentij.length; i+=2) {
			if((Client.currentij[i+1]+1)<(ClientFrame.MAX_H)){
				//client
				if(ClientFrame.p1[Client.currentij[i]][Client.currentij[i+1]+1].empty==0){
				}else {
					cham = false;
				}
			}
		}
		if(cham){
			int i = 0;
			for (int k = 1; k < Client.currentij.length; k+=2) {
				if(Client.currentij[k]<ClientFrame.MAX_H -1){
					i++;
				}
			}
			if(i==(Client.currentij.length/2)){
				for (int k = 1; k < Client.currentij.length; k+=2) {
					if(Client.currentij[k]<ClientFrame.MAX_H -1){
						Client.currentij[k]++;
					}
				}
			}
			
		}
	}
	
	public void moveLeft(){
		boolean cham = true;
		
		for (int i = 0; i <Client.currentij.length; i+=2) {
				//블록의 지금 위치 +1 이 패널 최대크기보다 작은것만 다음으로 넘어간다.
				if((Client.currentij[i+1]-1)>=0){
					//client
					if(ClientFrame.p1[Client.currentij[i]][Client.currentij[i+1]-1].empty==0){
					}else {
						cham = false;
					}
				}
		}
		if(cham){
			int i = 0;
			for (int k = 1; k < Client.currentij.length; k+=2) {
				if(Client.currentij[k]>=1){
					i++;
				}
			}
			if(i==(Client.currentij.length/2)){
				for (int k = 1; k < Client.currentij.length; k+=2) {
					if(Client.currentij[k]>=1){
						Client.currentij[k]--;
					}
				}
			}
			
		}
	}
	
	public void moveSpace(){
		while(true){
			boolean cham1 = true;
			for (int i = 0; i <Client.currentij.length; i+=2) {
					//블록의 지금 위치 +1 이 패널 최대크기보다 작은것만 다음으로 넘어간다.
					if((Client.currentij[i]+1)<(ClientFrame.MAX_V)){
						//client
						if(ClientFrame.p1[Client.currentij[i]+1][Client.currentij[i+1]].empty==0){
						}else {
							cham1 = false;
						}
					}else {
						cham1 = false;
					}
			}
			if(cham1){
				int i = 0;
				for (int k = 0; k < Client.currentij.length; k+=2) {
					if(Client.currentij[k]<ClientFrame.MAX_V -1){
						i++;
					}
				}
				if(i==(Client.currentij.length/2)){
					for (int k = 0; k < Client.currentij.length; k+=2) {
						if(Client.currentij[k]<ClientFrame.MAX_V -1){
							Client.currentij[k]++;
						}
					}
				}
			}else{
				break;
			}
		}
	}
	
	public void deleteBlock(){
		boolean cham3 = true;
		for (int i = 0; i < ClientFrame.p1.length; i++) {
			for (int j = 0; j < ClientFrame.p1[i].length; j++) {
				if(ClientFrame.p1[i][j].empty != 1){
					cham3 = false;
					break;
				}
			}
			if(cham3){
				for (int k = 0; k < ClientFrame.p1[i].length; k++) {
					ClientFrame.p1[i][k].empty=0;
					ClientFrame.p1[i][k].setBackground(ClientFrame.oColor());
				}
				Color c1 = new Color(155,155,155);
				for (int m = i-1; m > -1; m--) {
					for (int n = 0; n < ClientFrame.p1[m].length; n++) {
						if(ClientFrame.p1[m][n].empty == 1){
							c1 = ClientFrame.p1[m][n].getBackground();
							ClientFrame.p1[m][n].empty = 0;
							ClientFrame.p1[m][n].setBackground(ClientFrame.oColor());
							ClientFrame.p1[m+1][n].empty = 1;
							ClientFrame.p1[m+1][n].setBackground(c1);
							
						}
					}
				}
			}
			cham3 = true;
			Client.cfr.repaint();
		}
		
	}
	
	public void changeBackColor(){

		for (int i = 0; i <Client.currentij.length; i+=2) {
			ClientFrame.p1[Client.currentij[i]][Client.currentij[i+1]].setBackground(ClientFrame.fColor(Client.currentblocknum));
			ClientFrame.p1[Client.currentij[i]][Client.currentij[i+1]].empty=1;
			Client.cfr.repaint();
		}
		

	}
	
	public static void changeBlock(){


		Client.currentblocknum = Client.futureblocknum;
		for (int j = 0; j < Client.currentij.length; j++) {
			Client.currentij[j] = Client.arrblock[Client.currentblocknum].getIniarray(j);
		}
		Client.futureblocknum = (int) (Math.random()*Client.arrblock.length);
		for (int j = 0; j < Client.futureij.length; j++) {
			Client.futureij[j] = Client.arrblock[Client.futureblocknum].getIniarray(j);
		}
		
		int x, y;
		y = 2-Client.futureij[4];
		x = 2-Client.futureij[5];
		
		for (int j = 0; j < Client.futureij.length; j+=2) {
			Client.futureij[j] = y + Client.futureij[j];
			Client.futureij[j+1] = x + Client.futureij[j+1];
			
		}
		for (int i = 0; i <ClientFrame.p2.length; i++) {
			for (int j = 0; j < ClientFrame.p2[i].length; j++) {
				ClientFrame.p2[i][j].setBackground(ClientFrame.oColor());
			}

		}
		for (int i = 0; i <Client.futureij.length; i+=2) {
			ClientFrame.p2[Client.futureij[i]][Client.futureij[i+1]].setBackground(ClientFrame.fColor(Client.futureblocknum));

		}
		Client.cfr.repaint();
		block.numchanged = 0;
/*	바꾸기전 메소드
  		Client.currentblocknum = (int) (Math.random()*Client.arrblock.length);
		for (int j = 0; j < Client.currentij.length; j++) {
			Client.currentij[j] = Client.arrblock[Client.currentblocknum].getIniarray(j);
		}
		
		block.numchanged = 0;*/
	}
	

	public synchronized void keyPressed(KeyEvent e){
		while(Client.condition1 ==true){
			try{
				wait();
				
			}catch (InterruptedException z){
			}
		}
		Client.condition2 = true;
//		Client.t1.suspend();
		//움직이기전에 기존에있던 패널들의 색깔을 기본으로 만든다. empty는 비었다는 포현이다
		setBackColor();
		if(e.getKeyCode() == 32){
			
			//스페이스
			moveSpace();
			//삭제해야하는라인체크 있다면 다음 이프문에서 삭제하고 그위에 블록 한칸씩 아래로 내림
			
			changeBackColor();
			deleteBlock();
			changeBlock();
			for (int i = 0; i < ClientFrame.p1.length; i++) {
				for (int j = 0; j < ClientFrame.p1[i].length; j++) {
				}
			}//나중에 삭제
		} //end of 32
		if(e.getKeyCode() == 38){
			//위
			pushUp();
		}
		if(e.getKeyCode() == 40){
			//아래
			moveDown();
		}
		if(e.getKeyCode() == 39){
			//오른쪽
			moveRight();
		}
		if(e.getKeyCode() == 37){
			//왼쪽
			moveLeft();
		} //end of 37
			//움직인 블록만 색깔바꿈
		changeBackColor();
//		Client.t1.resume();
		notifyAll();
		Client.condition2 = false;
	}

}

class block{

	static int numchanged=0;
	public void changeBlockForm(int arr[]){
		
	}
	public int getIniarray(int arr){
		return arr;
	}
	
	public void checkLimit(int []arr){
		int a = 0;
		for (int i = 0; i < arr.length; i+=2) {
			if(arr[i]>ClientFrame.MAX_V-1){
				a=(ClientFrame.MAX_V-1)-arr[i];
				for (int j = 0; j < arr.length; j+=2) {
					arr[j] = arr[j] + a;
				}
			}
		}
		for (int i= 1; i < arr.length; i+=2) {
			if(arr[i]<0){
				a = -arr[i];
				for (int j = 1; j < arr.length; j+=2) {
					arr[j] = arr[j] + a;
				}
			}
		}
		for (int i = 1; i < arr.length; i+=2) {
			if(arr[i]>ClientFrame.MAX_H-1){
				a=(ClientFrame.MAX_H-1)-arr[i];
				for (int j = 1; j < arr.length; j+=2) {
					arr[j] = arr[j] + a;
				}
			}
		}
	}
}



//네모블록
class block1 extends block{
	static int []ij = {0, 5, 0, 6, 1, 5, 1, 6};

	@Override
	public void changeBlockForm(int arr[]){

	}
	public int getIniarray(int i){
		return ij[i];
	}
}

//직선블록
class block2 extends block{
	static int []ij = {0, 5, 1, 5, 2, 5, 3, 5};

	@Override
	public void changeBlockForm(int arr[]){
		if(numchanged==0){
			arr[0] += 2;
			arr[2] += 1;
			arr[6] -= 1;
			arr[1] -= 2;
			arr[3] -= 1;
			arr[7] += 1;
			numchanged++;
		} else if(numchanged==1){
			arr[0] -= 2;
			arr[2] -= 1;
			arr[6] += 1;
			arr[1] += 2;
			arr[3] += 1;
			arr[7] -= 1;
			numchanged = 0;
		}
		checkLimit(arr);
	}
	public int getIniarray(int i){
		return ij[i];
	}
}
//철구자
class block3 extends block{
	static int []ij = {0, 5, 1, 4, 1, 5, 1, 6};

	@Override
	public void changeBlockForm(int arr[]){
		if(numchanged==0){
			arr[2] -= 1;
			arr[3] += 1;
			arr[0] += 1;
			arr[1] += 1;
			arr[6] += 1;
			arr[7] -= 1;
			numchanged++;
		}else if(numchanged==1){
			arr[2] += 1;
			arr[3] += 1;
			arr[0] += 1;
			arr[1] -= 1;
			arr[6] -= 1;
			arr[7] -= 1;
			numchanged++;
		}else if(numchanged==2){
			arr[2] += 1;
			arr[3] -= 1;
			arr[0] -= 1;
			arr[1] -= 1;
			arr[6] -= 1;
			arr[7] += 1;
			numchanged++;
		}else if(numchanged==3){
			arr[2] -= 1;
			arr[3] -= 1;
			arr[0] -= 1;
			arr[1] += 1;
			arr[6] += 1;
			arr[7] += 1;
			numchanged=0;
		}
		checkLimit(arr);
	}
	public int getIniarray(int i){
		return ij[i];
	}
}

class block4 extends block{
	static int []ij = {0, 4, 0, 5, 1, 5, 1, 6};

	
	@Override
	public void changeBlockForm(int arr[]){
		if(numchanged==0){

			arr[1] += 2;
			arr[2] += 1;
			arr[3] += 1;
			arr[6] += 1;
			arr[7] -= 1;
			numchanged++;
		}else if(numchanged==1){

			arr[1] -= 2;
			arr[2] -= 1;
			arr[3] -= 1;
			arr[6] -= 1;
			arr[7] += 1;
			numchanged=0;
		}
		checkLimit(arr);
	}
	public int getIniarray(int i){
		return ij[i];
	}
}

class block5 extends block{
	static int []ij = {0, 6, 1, 4, 1, 5, 1, 6};

	@Override
	public void changeBlockForm(int arr[]){
		if(numchanged==0){
			arr[0] += 2;
			arr[2] -= 1;
			arr[3] += 1;
			arr[6] += 1;
			arr[7] -= 1;
			numchanged++;
		}else if(numchanged==1){
			arr[1] -= 2;
			arr[2] += 1;
			arr[3] += 1;
			arr[6] -= 1;
			arr[7] -= 1;
			numchanged++;
		}else if(numchanged==2){
			arr[0] -= 2;

			arr[2] += 1;
			arr[3] -= 1;
			arr[6] -= 1;
			arr[7] += 1;
			numchanged++;
		}else if(numchanged==3){

			arr[1] += 2;
			arr[2] -= 1;
			arr[3] -= 1;
			arr[6] += 1;
			arr[7] += 1;
			numchanged=0;
		}
		checkLimit(arr);
	}
	public int getIniarray(int i){
		return ij[i];
	}
}


class block6 extends block{
	static int []ij = {0, 6, 0, 5, 1, 5, 1, 4};

	@Override
	public void changeBlockForm(int arr[]){
		if(numchanged==0){
			
			arr[1] -= 2;
			arr[2] += 1;
			arr[3] -= 1;
			arr[6] += 1;
			arr[7] += 1;
			numchanged++;
		}else if(numchanged==1){
			arr[1] += 2;
			arr[2] -= 1;
			arr[3] += 1;
			arr[6] -= 1;
			arr[7] -= 1;
			numchanged=0;
		}
		checkLimit(arr);
	}
	public int getIniarray(int i){
		return ij[i];
	}
}

class block7 extends block{
	static int []ij = {0, 4, 1, 4, 1, 5, 1, 6};

	@Override
	public void changeBlockForm(int arr[]){
		if(numchanged==0){
	
			arr[1] += 2;
			arr[2] -= 1;
			arr[3] += 1;
			arr[6] += 1;
			arr[7] -= 1;
			numchanged++;
		}else if(numchanged==1){
			arr[0] += 2;

			arr[2] += 1;
			arr[3] += 1;
			arr[6] -= 1;
			arr[7] -= 1;
			numchanged++;
		}else if(numchanged==2){

			arr[1] -= 2;
			arr[2] += 1;
			arr[3] -= 1;
			arr[6] -= 1;
			arr[7] += 1;
			numchanged++;
		}else if(numchanged==3){

			arr[0] -= 2;
			arr[2] -= 1;
			arr[3] -= 1;
			arr[6] += 1;
			arr[7] += 1;
			numchanged=0;
		}
		checkLimit(arr);
	}
	public int getIniarray(int i){
		return ij[i];
	}
}

class Windowcontrol implements WindowListener{

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
	
}
	