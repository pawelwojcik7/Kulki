package com.company;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.lang.Object;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;

public class Panel extends JPanel {
    JFrame okno = null;
    protected class Zderzenia{
        public int x1,x2,y1,y2,size1,size2;
        public Color kolor1,kolor2;

        public Zderzenia() {
        }

        public Zderzenia(Kula a, Kula b) {
            this.x1 = a.x;
            this.y1 = a.y;
            this.size1 = a.size;
            this.kolor1 = a.kolor;
            this.x2 = b.x;

            this.y2 = b.y;
            this.size2 = b.size;
            this.kolor2 = b.kolor;
        }
        public void zapisz(ArrayList<Zderzenia> a) throws IOException{
            String linia = new String();
            Zderzenia x;
            x=a.get(a.size()-1);
            linia = x.x1 + " " + x.y1 + " " + x.size1+" "+x.kolor1.getRed() +" "+x.kolor1.getGreen()+" "+x.kolor1.getBlue() + " " + x.x2 + " " + x.y2 + " " + x.size2 +" "+x.kolor2.getRed()+" "+x.kolor2.getGreen()+" "+x.kolor2.getBlue()+ "\n";
            plik.zapisz(linia);
        }
    }
    Plik plik;{
        try {
            plik = new Plik("test.txt");
        } catch (IOException w) {
            w.printStackTrace();
        }
    }

    private ArrayList<Kula> listaKul;
    private int size = 20;
    private Timer timer;
    private final int DELAY = 33;

    public ArrayList<Zderzenia> listazderzen = new ArrayList<>();
    Zderzenia zderzenie;



    public Panel() {
        listaKul = new ArrayList<>();
        setBackground(Color.BLACK);

        addMouseListener(new Listener());
        addMouseMotionListener(new Listener());
        addMouseWheelListener(new Listener());

        timer = new Timer(DELAY, new Listener());
        timer.start();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Kula k : listaKul) {

            g.setColor(k.kolor);
            g.fillOval(k.x - k.size / 2, k.y - k.size / 2, k.size, k.size);

        }

        g.setColor(Color.YELLOW);
        g.drawString(Integer.toString(listaKul.size()), 40, 40);
        g.drawString("do pliku zapisuje rowniez skladowe rgb kulek, kolizje nie zawsze sie idealnie rysuja, wynika to z rzutowania doubla na inta skladowych wektorow po kolizji", 15, 15);

    }

    private class Listener implements MouseListener, MouseMotionListener, MouseWheelListener, ActionListener {

        @Override
        public void mouseClicked(MouseEvent mouseEvent) {

        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            listaKul.add(new Kula(mouseEvent.getX(), mouseEvent.getY(), size));
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {
            timer.start();

            if (okno != null) {
                if (okno.isActive()) {
                    okno.dispose();

                }
            }
        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {
            if (listazderzen.size() != 0) {
                timer.stop();
                okno = new JFrame("Zderzenia");
                okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                try {
                    okno.getContentPane().add(new Okno(okno, plik));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                okno.setPreferredSize(new Dimension(800,800));
                okno.setLocation(800, 0);
                okno.pack();
                okno.setVisible(true);
            }

        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            for (int i = 0; i < listaKul.size(); i++) {
                listaKul.get(i).update();
                for (int j = i + 1; j < listaKul.size(); j++) {
                    if (listaKul.get(i).warunekKolizji(listaKul.get(j))) {
                        listaKul.get(i).kolizja(listaKul.get(j));
                    }
                }
                repaint();
            }

        }

        @Override
        public void mouseDragged(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseMoved(MouseEvent mouseEvent) {

        }


        @Override
        public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
            if (mouseWheelEvent.getWheelRotation() < 0) {
                if (size <= 100) size += 5;
            } else {
                if (size >= 10) size -= 5;

            }
        }
    }

    public class Kula {

        public int x, y, size;
        double xspeed, yspeed;
        public boolean xc = false, yc = false;
        public Color kolor;
        private final double MAX_SPEED = 5;

        public Kula(int x, int y, int size) {
            this.x = x;
            this.y = y;
            this.size = size;


            kolor = new Color((float) Math.random(), (float) Math.random(), (float) Math.random());


            while (xspeed == 0 || yspeed == 0) {
                xspeed = (int) (Math.random() * MAX_SPEED * 2 - MAX_SPEED);

                yspeed = (int) (Math.random() * MAX_SPEED * 2 - MAX_SPEED);
            }





        }
        public Kula(int x, int y, int size,int r, int g,int b ) {
            this.x = x;
            this.y = y;
            this.size = size;


            kolor = new Color( r,  g,  b);




        }

        public void update() {
            x += xspeed;
            y += yspeed;

            if(this.x+this.size/2> getWidth())
            {
                this.x=getWidth()-this.size/2;
                this.xspeed=-this.xspeed;
            }
            if(this.x-this.size/2<0)
            {
                this.x=0+this.size/2;
                this.xspeed=-this.xspeed;
            }
            if(this.y+this.size/2> getHeight())
            {
                this.y=getHeight()-this.size/2;
                this.yspeed=-this.yspeed;
            }
            if(this.y-this.size/2< 0)
            {
                this.y=0+this.size/2;
                this.yspeed=-this.yspeed;
            }
        }

        public boolean warunekKolizji(Kula k) {
            double d;
            double r1 = (double) this.size / 2;
            double r2 = (double) k.size / 2;
            d = Math.sqrt((k.x - this.x) * (k.x - this.x) + (k.y - this.y) * (k.y - this.y));
            if (d <= (r1 + r2)) {
                if(k.x<x){
                    k.x-=2;
                    x+=2;
                }
                else{
                    k.x+=2;
                    x-=2;
                }
                if(k.y<y){
                    k.y-=2;
                    y+=2;
                }
                else{
                    k.y+=2;
                    y-=2;
                }
            }
            return d<=(r1+r2);

        }

        public void kolizja(Kula k) {
                int mass1, mass2;
                mass1 = (this.size / 2) ^ 3;
                mass2 = (k.size / 2) ^ 3;
                int xdl, ydl;
                xdl = this.x - k.x;
                ydl = this.y - k.y;
                double angle = Math.atan2(ydl, xdl);
                double v1, v2;
                v1 = Math.sqrt(this.xspeed * this.xspeed + this.yspeed * this.yspeed);
                v2 = Math.sqrt(k.xspeed * k.xspeed + k.yspeed * k.yspeed);
                double angle1, angle2;
                angle1 = Math.atan2(this.yspeed, this.xspeed);
                angle2 = Math.atan2(k.yspeed, k.xspeed);
                double pom1, pom2, pom3, pom4;
                pom1 = v1 * Math.cos(angle1 - angle);
                pom2 = v1 * Math.sin(angle1 - angle);
                pom3 = v2 * Math.cos(angle2 - angle);
                pom4 = v2 * Math.sin(angle2 - angle);
                double fxspeed1, fyspeed1, fxspeed2, fyspeed2;
                fxspeed1 = ((mass1 - mass2) * pom1 + (mass2 + mass2) * pom3) / (mass1 + mass2);
                fxspeed2 = ((mass1 + mass1) * pom1 + (mass2 - mass1) * pom3) / (mass1 + mass2);
                fyspeed1 = pom2;
                fyspeed2 = pom4;
                this.xspeed = Math.cos(angle) * fxspeed1 + Math.cos(angle + Math.PI / 2) * fyspeed1;
                this.yspeed = Math.sin(angle) * fxspeed1 + Math.sin(angle + Math.PI / 2) * fyspeed1;
                k.xspeed = Math.cos(angle) * fxspeed2 + Math.cos(angle + Math.PI / 2) * fyspeed2;
                k.yspeed = Math.sin(angle) * fxspeed2 + Math.sin(angle + Math.PI / 2) * fyspeed2;
                zderzenie=new Zderzenia(this,k);
                listazderzen.add(zderzenie);
                try{
                    zderzenie.zapisz(listazderzen);
                }catch (IOException e) {
                    e.printStackTrace();
                }





        }
    }

    public class Okno extends JPanel {
        JFrame okno;
        Plik plik;
        ArrayList<Panel.Zderzenia> lZderzenia;




        public ArrayList<Panel.Zderzenia> wczytaj() throws FileNotFoundException {
            Scanner pom = new Scanner(plik.file);
            Panel.Kula k1, k2;
            ArrayList<Panel.Zderzenia> lista = new ArrayList<>();
            StringTokenizer token;


            int i=0;
            while (pom.hasNextLine()){
                if(i < lista.size()-1){
                    i++;
                } else{
                    token = new StringTokenizer(pom.nextLine());
                    k1 = new Kula(Integer.parseInt(token.nextToken()), Integer.parseInt(token.nextToken()), Integer.parseInt(token.nextToken()),Integer.parseInt(token.nextToken()),Integer.parseInt(token.nextToken()),Integer.parseInt(token.nextToken()));
                    k2 = new Kula(Integer.parseInt(token.nextToken()), Integer.parseInt(token.nextToken()), Integer.parseInt(token.nextToken()),Integer.parseInt(token.nextToken()),Integer.parseInt(token.nextToken()),Integer.parseInt(token.nextToken()));
                    lista.add(new Panel.Zderzenia(k1, k2));

                }
            }
            pom.close();
            return lista;
        }

        public Okno(JFrame okno, Plik plik) throws FileNotFoundException {
            lZderzenia = new ArrayList<>();
            setBackground(Color.BLACK);
            this.okno = okno;
            this.plik = plik;

            lZderzenia=wczytaj();
            repaint();

        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(Color.BLACK);

            for (Panel.Zderzenia k : lZderzenia) {
                g.setColor(k.kolor1);
                g.fillOval(k.x1 - k.size1 / 2, k.y1 - k.size1 / 2, k.size1, k.size1);
                g.setColor(k.kolor2);
                g.fillOval(k.x2 - k.size2 / 2, k.y2 - k.size2 / 2, k.size2, k.size2);
            }
        }
    }




}

