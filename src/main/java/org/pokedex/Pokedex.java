package org.pokedex;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.Color;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStreamReader;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

// RoundedButton
class RoundedButton extends JButton {
    public RoundedButton(String label) {
        super(label);
        setContentAreaFilled(false);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(Color.decode("#C1364D"));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(Color.decode("#000000"));
            }
        });
    }

    public RoundedButton(ImageIcon label) {
        super(label);
        setContentAreaFilled(false);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(Color.decode("#C1364D"));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(Color.decode("#000000"));
            }
        });
    }

    protected void paintComponent(Graphics g) {
        if (getModel().isArmed()) {
            g.setColor(Color.decode("#BA263E"));
        } else {
            g.setColor(getBackground());
        }
        g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
        super.paintComponent(g);
    }

    protected void paintBorder(Graphics g) {
        g.setColor(Color.decode("#BA263E"));
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
    }

    Shape shape;

    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
        }
        return shape.contains(x, y);
    }
}

// RoundedTextField

abstract class PokeTextField extends JTextField {
    public PokeTextField(int size) {
        super(size);
        setOpaque(false);
    }

    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
        super.paintComponent(g);
    }
    protected void paintBorder(Graphics g) {
        g.setColor(Color.decode("#BA263E"));
        g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
    }
    public abstract boolean contains(int x, int y);
}

class RoundedTextField extends PokeTextField {
    private Shape shape;

    public RoundedTextField(int size) {
        super(size);
        setOpaque(false);
    }

    @Override
    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 20, 20);
        }
        return shape.contains(x, y);
    }
}

class RoundedTextArea extends JTextArea {
    public RoundedTextArea(int rows, int columns) {
        super(rows, columns);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    }
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
        super.paintComponent(g);
    }
    protected void paintBorder(Graphics g) {
        g.setColor(Color.decode("#BA263E"));
        g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
    }
    public boolean contains(int x, int y) {
        return super.contains(x, y);
    }
}

class RoundedProgressBarUI extends BasicProgressBarUI {
    private static final int ARC_WIDTH = 20;
    private static final int ARC_HEIGHT = 20;

    @Override
    protected void paintDeterminate(Graphics g, JComponent c) {
        Graphics2D g2 = (Graphics2D) g;
        Insets b = progressBar.getInsets();
        int width = progressBar.getWidth();
        int height = progressBar.getHeight();
        int barRectWidth = width - (b.right + b.left);
        int barRectHeight = height - (b.top + b.bottom);

        g2.setColor(progressBar.getBackground());
        g2.fillRoundRect(b.left, b.top, barRectWidth, barRectHeight, ARC_WIDTH, ARC_HEIGHT);

        int amountFull = getAmountFull(b, barRectWidth, barRectHeight);

        if (progressBar.getOrientation() == SwingConstants.HORIZONTAL) {
            g2.setColor(progressBar.getForeground());
            g2.fillRoundRect(b.left, b.top, amountFull, barRectHeight, ARC_WIDTH, ARC_HEIGHT);
        } else {
            g2.setColor(progressBar.getForeground());
            g2.fillRoundRect(b.left, b.top + (barRectHeight - amountFull), barRectWidth, amountFull, ARC_WIDTH, ARC_HEIGHT);
        }

        if (progressBar.isStringPainted()) {
            paintString(g, b.left, b.top, barRectWidth, barRectHeight, amountFull, b);
        }
    }
}

class RoundedProgressBar extends JProgressBar {
    public RoundedProgressBar() {
        super();
        setOpaque(false);
        setUI(new RoundedProgressBarUI());
        setFont(new Font("Ariel", Font.PLAIN, 18));
        setStringPainted(true);
        setForeground(Color.decode("#BA263E"));
        setBackground(Color.decode("#000000"));
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
    protected void paintBorder(Graphics g) {
        g.setColor(Color.decode("#BA263E"));
        g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
    }

    public boolean contains(int x, int y) {
        return super.contains(x, y);
    }
}

class RoundedDialog extends JDialog {
    public RoundedDialog(Frame parent, String title, String message) {
        super(parent, title, true);
        setLayout(null);
        setSize(500, 170);

        setContentPane(new JLabel(new ImageIcon("src/assets/background.jpg")));

        JLabel label = new JLabel(message, SwingConstants.LEFT);
        label.setForeground(Color.decode("#FFFFFF"));
        label.setFont(new Font("Ariel", Font.PLAIN, 18));
        label.setBounds(10,10,440,30);
        add(label);

        RoundedButton okButton = new RoundedButton("OK");
        okButton.setBounds(380,70,100,40);
        okButton.setBackground(Color.decode("#000000"));
        okButton.setForeground(Color.decode("#FFFFFF"));
        okButton.setFocusPainted(false);
        okButton.setFont(new Font("Ariel", Font.PLAIN, 18));
        add(okButton);
        setLocationRelativeTo(parent);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}


// PokeDetails

class PokemonDetails {
    private String pName;
    private String api_url;
    private JSONObject pData;

    public PokemonDetails(String pName) {
        this.pName = pName;
        this.api_url = "https://pokeapi.co/api/v2/pokemon/" + this.pName;
        fetchData();
    }

    private void fetchData() {
        try {
            URL url = new URL(api_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            int status = conn.getResponseCode();

            if (status != 200) {
                throw new Exception("Failed to fetch data. Status code: " + status);
            } else {
                JSONParser parser = new JSONParser();
                JSONObject responseData = (JSONObject) parser.parse(new InputStreamReader(conn.getInputStream()));

                // Extracting required data
                pData = responseData;
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ImageIcon getPokemonImage() {
        if (pData == null) {
            return null;
        }

        JSONObject sprites = (JSONObject) pData.get("sprites");
        JSONObject other = (JSONObject) sprites.get("other");
        JSONObject officialArtwork = (JSONObject) other.get("official-artwork");
        String spriteUrl = (String) officialArtwork.get("front_default");
        ImageIcon pokeImg = null;
        try {
            URL url = new URL(spriteUrl);
            BufferedImage pokeImgRaw = ImageIO.read(url);
            Image scaledImg = pokeImgRaw.getScaledInstance(250, 250, Image.SCALE_DEFAULT);
            pokeImg = new ImageIcon(scaledImg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pokeImg;
    }

    public ArrayList<Long> getPokeStats() {
        ArrayList<Long> statsList = new ArrayList<Long>();
        if (pData == null) {
            return statsList;
        }

        JSONArray stats = (JSONArray) pData.get("stats");
        for (Object o : stats) {
            JSONObject stat = (JSONObject) o;
            statsList.add((Long) stat.get("base_stat"));
        }

        return statsList;
    }

    public String getPokeAbilities() {
        String abilitiesStr = "";
        if (pData == null) {
            return abilitiesStr;
        }

        JSONArray abilities = (JSONArray) pData.get("abilities");
        for (Object o : abilities) {
            JSONObject ability = (JSONObject) o;
            JSONObject abilityName = (JSONObject) ability.get("ability");
            abilitiesStr += abilityName.get("name") + "\n";
        }

        return abilitiesStr;
    }

    public String getPokeTypes() {
        String typesStr = "";
        if (pData == null) {
            return typesStr;
        }

        JSONArray types = (JSONArray) pData.get("types");
        for (Object typeData : types) {
            JSONObject typeObj = (JSONObject) typeData;
            JSONObject type = (JSONObject) typeObj.get("type");
            String typeName = (String) type.get("name");
            typesStr += typeName + "\n";
        }

        return typesStr;
    }

    public void downloadPokemonImage(JFrame rootFrame) throws FileNotFoundException, MalformedURLException, FileAlreadyExistsException {
        if (pData == null) {
            RoundedDialog dialog = new RoundedDialog(rootFrame,"Error","Unable to capture pokemon, Please try again later.");
            dialog.setVisible(true);
        }

        JSONObject sprites = (JSONObject) pData.get("sprites");
        if (sprites == null) {
            RoundedDialog dialog = new RoundedDialog(rootFrame,"Error","Unable to capture pokemon, Please try again later.");
            dialog.setVisible(true);
        }
        JSONObject other = (JSONObject) sprites.get("other");
        JSONObject officialArtwork = (JSONObject) other.get("official-artwork");
        String spriteUrl = (String) officialArtwork.get("front_default");

        URL url = new URL(spriteUrl);
        try (InputStream pokeImgRaw = url.openStream()) {
            Files.copy(pokeImgRaw, Paths.get("src/images/"+pName.toLowerCase()+".png"));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void savePokemonDetails() {
        JSONObject pokeDetails = new JSONObject();
        pokeDetails.put("name", pName.toUpperCase());
        pokeDetails.put("stats", getPokeStats());
        pokeDetails.put("abilities", getPokeAbilities());
        pokeDetails.put("types", getPokeTypes());

        try {
            FileWriter fileWriter = new FileWriter("src/json/"+pName.toLowerCase()+".json");
            fileWriter.write(pokeDetails.toJSONString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}



class PokemonCaptureTask implements Runnable {
    private String pokemonName;
    private JFrame rootFrame;

    public PokemonCaptureTask(JFrame rootFrame,String pokemonName) {
        this.pokemonName = pokemonName.toLowerCase();
        this.rootFrame = rootFrame;
    }

    @Override
    public void run() {
        PokemonDetails pCapture = new PokemonDetails(pokemonName);
        try {
            pCapture.downloadPokemonImage(rootFrame);
            pCapture.savePokemonDetails();
            RoundedDialog dialog = new RoundedDialog(rootFrame,"Success","Pokemon captured successfully!");
            dialog.setVisible(true);
        }
        catch (FileNotFoundException | MalformedURLException | FileAlreadyExistsException ex) {
            RoundedDialog dialog = new RoundedDialog(rootFrame,"Error","Unable to capture pokemon, Please try again later.");
            dialog.setVisible(true);
            throw new RuntimeException(ex);
        }
    }
}



public class Pokedex extends JFrame {
    private JFrame rootF = this;
    private JTextField pokeTextField;
    private RoundedButton searchBtn, captureBtn, displayBtn, preBtn, nextBtn;
    private RoundedTextArea statsTextArea, abilitiesTextArea, typesTextArea;
    private JLabel pokeNameLabel, pokeTypeLabel, pokeAbilityLabel, pokeStatsLabel;
    private RoundedProgressBar hpBar, attackBar, defenseBar, spAttackBar, spDefenseBar, speedBar;

    public Pokedex() throws IOException {
        setTitle("Pokédex");
        setSize(850, 480);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(new JLabel(new ImageIcon("src/assets/background.jpg")));
        ImageIcon FrameIcon = new ImageIcon("src/assets/pokeIcon.png");
        setIconImage(FrameIcon.getImage());

        pokeTextField = new RoundedTextField(20);
        pokeTextField.setCaretColor(Color.decode("#FFFFFF"));
        pokeTextField.setFont(new Font("Ariel", Font.PLAIN, 18));
        pokeTextField.setBounds(10, 10, 220, 50);
        pokeTextField.setBackground(Color.decode("#000000"));
        pokeTextField.setBorder(BorderFactory.createLineBorder(Color.decode("#BA263E")));
        pokeTextField.setForeground(Color.decode("#FFFFFF"));
        add(pokeTextField);

        BufferedImage searchBuffImg = ImageIO.read(new File("src/assets/search.png"));
        Image searchImg = searchBuffImg.getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon searchIcon = new ImageIcon(searchImg);

        searchBtn = new RoundedButton(searchIcon);
        searchBtn.setFocusPainted(false);
        searchBtn.setFocusPainted(false);
        searchBtn.setBounds(240, 10, 50, 50);
        searchBtn.setBackground(Color.decode("#000000"));
        searchBtn.setForeground(Color.decode("#FFFFFF"));
        searchBtn.setFocusPainted(false);
        add(searchBtn);

        captureBtn = new RoundedButton("Capture");
        captureBtn.setFocusPainted(false);
        captureBtn.setFont(new Font("Ariel", Font.PLAIN, 16));
        captureBtn.setBounds(10, 70, 135, 40);
        captureBtn.setBackground(Color.decode("#000000"));
        captureBtn.setForeground(Color.decode("#FFFFFF"));
        add(captureBtn);

        displayBtn = new RoundedButton("Display");
        displayBtn.setFocusPainted(false);
        displayBtn.setFont(new Font("Ariel", Font.PLAIN, 16));
        displayBtn.setBounds(155, 70, 135, 40);
        displayBtn.setBackground(Color.decode("#000000"));
        displayBtn.setForeground(Color.decode("#FFFFFF"));
        add(displayBtn);


        BufferedImage bufferedImageArtwork = ImageIO.read(new File("src/assets/25.png"));
        Image pokeImg = bufferedImageArtwork.getScaledInstance(250, 250, Image.SCALE_DEFAULT);
        ImageIcon pokeIcon = new ImageIcon(pokeImg);

        JLabel pokeLabel = new JLabel(pokeIcon);
        pokeLabel.setBounds(10,130,280,240);
        pokeLabel.setForeground(Color.decode("#FFFFFF"));
        pokeLabel.setVisible(false);
        add(pokeLabel);

        preBtn = new RoundedButton("Previous");
        preBtn.setFocusPainted(false);
        preBtn.setFont(new Font("Ariel", Font.PLAIN, 16));
        preBtn.setBounds(10, 380, 135, 40);
        preBtn.setBackground(Color.decode("#000000"));
        preBtn.setForeground(Color.decode("#FFFFFF"));
        preBtn.setVisible(false);
        add(preBtn);

        nextBtn = new RoundedButton("Next");
        nextBtn.setFocusPainted(false);
        nextBtn.setFont(new Font("Ariel", Font.PLAIN, 16));
        nextBtn.setBounds(155, 380, 135, 40);
        nextBtn.setBackground(Color.decode("#000000"));
        nextBtn.setForeground(Color.decode("#FFFFFF"));
        nextBtn.setVisible(false);
        add(nextBtn);

        pokeNameLabel = new JLabel("");
        pokeNameLabel.setFont(new Font("Ariel", Font.PLAIN, 26));
        pokeNameLabel.setBounds(450, 10, 250, 30);
        pokeNameLabel.setForeground(Color.decode("#FFFFFF"));
        pokeNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pokeNameLabel.setVisible(false);
        add(pokeNameLabel);

        pokeStatsLabel = new JLabel("Stats");
        pokeStatsLabel.setFont(new Font("Ariel", Font.PLAIN, 20));
        pokeStatsLabel.setBounds(320, 60, 250, 30);
        pokeStatsLabel.setForeground(Color.decode("#FFFFFF"));
        pokeStatsLabel.setVisible(false);
        add(pokeStatsLabel);


        hpBar = new RoundedProgressBar();
        hpBar.setBounds(320, 100, 250, 40);
        hpBar.setString("HP");
        hpBar.setValue(0);
        hpBar.setVisible(false);
        add(hpBar);

        attackBar = new RoundedProgressBar();
        attackBar.setBounds(320, 155, 250, 40);
        attackBar.setString("Attack");
        attackBar.setValue(0);
        attackBar.setVisible(false);
        add(attackBar);

        defenseBar = new RoundedProgressBar();
        defenseBar.setBounds(320, 210, 250, 40);
        defenseBar.setString("Defense");
        defenseBar.setValue(0);
        defenseBar.setVisible(false);
        add(defenseBar);

        spAttackBar = new RoundedProgressBar();
        spAttackBar.setBounds(320, 265, 250, 40);
        spAttackBar.setString("Sp. Attack");
        spAttackBar.setValue(0);
        spAttackBar.setVisible(false);
        add(spAttackBar);

        spDefenseBar = new RoundedProgressBar();
        spDefenseBar.setBounds(320, 320, 250, 40);
        spDefenseBar.setString("Sp. Defense");
        spDefenseBar.setValue(0);
        spDefenseBar.setVisible(false);
        add(spDefenseBar);

        speedBar = new RoundedProgressBar();
        speedBar.setBounds(320, 375, 250, 40);
        speedBar.setString("Speed");
        speedBar.setValue(0);
        speedBar.setVisible(false);
        add(speedBar);

        pokeAbilityLabel = new JLabel("Abilities");
        pokeAbilityLabel.setFont(new Font("Ariel", Font.PLAIN, 20));
        pokeAbilityLabel.setBounds(580, 60, 250, 30);
        pokeAbilityLabel.setForeground(Color.decode("#FFFFFF"));
        pokeAbilityLabel.setVisible(false);
        add(pokeAbilityLabel);

        abilitiesTextArea = new RoundedTextArea(5, 20);
        abilitiesTextArea.setFont(new Font("Ariel", Font.PLAIN, 18));
        abilitiesTextArea.setBounds(580, 100, 250, 130);
        abilitiesTextArea.setBackground(Color.decode("#000000"));
        abilitiesTextArea.setForeground(Color.decode("#FFFFFF"));
        abilitiesTextArea.setEditable(false);
        abilitiesTextArea.setVisible(false);
        add(abilitiesTextArea);

        pokeTypeLabel = new JLabel("Types");
        pokeTypeLabel.setFont(new Font("Ariel", Font.PLAIN, 18));
        pokeTypeLabel.setBounds(580, 250, 250, 30);
        pokeTypeLabel.setForeground(Color.decode("#FFFFFF"));
        pokeTypeLabel.setVisible(false);
        add(pokeTypeLabel);

        typesTextArea = new RoundedTextArea(5, 20);
        typesTextArea.setFont(new Font("Ariel", Font.PLAIN, 20));
        typesTextArea.setBounds(580, 290, 250, 130);
        typesTextArea.setBackground(Color.decode("#000000"));
        typesTextArea.setForeground(Color.decode("#FFFFFF"));
        typesTextArea.setEditable(false);
        typesTextArea.setVisible(false);
        add(typesTextArea);

        // Add action listeners
        pokeTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    searchBtn.doClick();
                }
            }
        });



        // Button action listeners
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Hide UI components before starting the task
                pokeLabel.setVisible(false);
                preBtn.setVisible(false);
                nextBtn.setVisible(false);
                pokeNameLabel.setVisible(false);
                pokeStatsLabel.setVisible(false);
                hpBar.setVisible(false);
                attackBar.setVisible(false);
                defenseBar.setVisible(false);
                spAttackBar.setVisible(false);
                spDefenseBar.setVisible(false);
                speedBar.setVisible(false);
                pokeAbilityLabel.setVisible(false);
                abilitiesTextArea.setVisible(false);
                pokeTypeLabel.setVisible(false);
                typesTextArea.setVisible(false);

                Runnable task = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            PokemonDetails p1 = new PokemonDetails(pokeTextField.getText().toLowerCase());
                            ArrayList<Long> stats = p1.getPokeStats();
                            String abilities = p1.getPokeAbilities();
                            String types = p1.getPokeTypes();
                            ImageIcon pokemonImage = p1.getPokemonImage();

                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    pokeNameLabel.setText(pokeTextField.getText().toUpperCase());
                                    hpBar.setValue(Math.toIntExact(stats.get(0)));
                                    hpBar.setString("HP : " + stats.get(0).toString());

                                    attackBar.setValue(Math.toIntExact(stats.get(1)));
                                    attackBar.setString("Attack : " + stats.get(1).toString());

                                    defenseBar.setValue(Math.toIntExact(stats.get(2)));
                                    defenseBar.setString("Defense : " + stats.get(2).toString());

                                    spAttackBar.setValue(Math.toIntExact(stats.get(3)));
                                    spAttackBar.setString("Special Attack : " + stats.get(3).toString());

                                    spDefenseBar.setValue(Math.toIntExact(stats.get(4)));
                                    spDefenseBar.setString("Special Defense : " + stats.get(4).toString());

                                    speedBar.setValue(Math.toIntExact(stats.get(5)));
                                    speedBar.setString("Speed : " + stats.get(5).toString());

                                    abilitiesTextArea.setText(abilities);
                                    typesTextArea.setText(types);

                                    pokeNameLabel.setVisible(true);
                                    pokeStatsLabel.setVisible(true);
                                    hpBar.setVisible(true);
                                    attackBar.setVisible(true);
                                    defenseBar.setVisible(true);
                                    spAttackBar.setVisible(true);
                                    spDefenseBar.setVisible(true);
                                    speedBar.setVisible(true);
                                    pokeAbilityLabel.setVisible(true);
                                    abilitiesTextArea.setVisible(true);
                                    pokeTypeLabel.setVisible(true);
                                    typesTextArea.setVisible(true);
                                    pokeLabel.setIcon(pokemonImage);
                                    pokeLabel.setVisible(true);
                                }
                            });
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                };

                new Thread(task).start();
            }
        });

        captureBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pokemonName = pokeTextField.getText();
                Runnable task = new PokemonCaptureTask(rootF,pokemonName);
                Thread thread = new Thread(task);
                thread.start();
            }
        });



        ImageManager imgManager = new ImageManager();

        displayBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pokeLabel.setVisible(true);
                preBtn.setVisible(true);
                nextBtn.setVisible(true);

                imgManager.refresh();
                ArrayList<Long> stats = imgManager.getCurrentStats();

                pokeNameLabel.setText(pokeTextField.getText().toUpperCase());
                hpBar.setValue(Math.toIntExact(stats.get(0)));
                hpBar.setString("HP : " + stats.get(0).toString());

                attackBar.setValue(Math.toIntExact(stats.get(1)));
                attackBar.setString("Attack : " + stats.get(1).toString());

                defenseBar.setValue(Math.toIntExact(stats.get(2)));
                defenseBar.setString("Defense : " + stats.get(2).toString());

                spAttackBar.setValue(Math.toIntExact(stats.get(3)));
                spAttackBar.setString("Special Attack : " + stats.get(3).toString());

                spDefenseBar.setValue(Math.toIntExact(stats.get(4)));
                spDefenseBar.setString("Special Defense : " + stats.get(4).toString());

                speedBar.setValue(Math.toIntExact(stats.get(5)));
                speedBar.setString("Speed : " + stats.get(5).toString());

                String abilities = imgManager.getCurAbilities();
                abilitiesTextArea.setText(abilities);

                String types = imgManager.getCurTypes();
                typesTextArea.setText(types);


                BufferedImage bufferedImageNext = null;
                try {
                    bufferedImageNext = ImageIO.read(new File("src/images/" + imgManager.getCurImg()));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                Image pokeImg = bufferedImageNext.getScaledInstance(250, 250, Image.SCALE_DEFAULT);
                ImageIcon pokemonImage = new ImageIcon(pokeImg);

                String pokeName = imgManager.getCurName();
                pokeNameLabel.setText(pokeName);

                pokeNameLabel.setVisible(true);
                pokeStatsLabel.setVisible(true);
                hpBar.setVisible(true);
                attackBar.setVisible(true);
                defenseBar.setVisible(true);
                spAttackBar.setVisible(true);
                spDefenseBar.setVisible(true);
                speedBar.setVisible(true);
                pokeAbilityLabel.setVisible(true);
                abilitiesTextArea.setVisible(true);
                pokeTypeLabel.setVisible(true);
                typesTextArea.setVisible(true);
                pokeLabel.setIcon(pokemonImage);
                pokeLabel.setVisible(true);
            }
        });


        preBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imgManager.preImg();
                BufferedImage bufferedImagePrevious = null;
                try {
                    bufferedImagePrevious = ImageIO.read(new File("src/images/" + imgManager.getCurImg()));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                Image pokeImg = bufferedImagePrevious.getScaledInstance(250, 250, Image.SCALE_DEFAULT);
                ImageIcon pokeIcon = new ImageIcon(pokeImg);
                pokeLabel.setIcon(pokeIcon);



                ArrayList<Long> stats = imgManager.getCurrentStats();

                pokeNameLabel.setText(pokeTextField.getText().toUpperCase());
                hpBar.setValue(Math.toIntExact(stats.get(0)));
                hpBar.setString("HP : " + stats.get(0).toString());

                attackBar.setValue(Math.toIntExact(stats.get(1)));
                attackBar.setString("Attack : " + stats.get(1).toString());

                defenseBar.setValue(Math.toIntExact(stats.get(2)));
                defenseBar.setString("Defense : " + stats.get(2).toString());

                spAttackBar.setValue(Math.toIntExact(stats.get(3)));
                spAttackBar.setString("Special Attack : " + stats.get(3).toString());

                spDefenseBar.setValue(Math.toIntExact(stats.get(4)));
                spDefenseBar.setString("Special Defense : " + stats.get(4).toString());

                speedBar.setValue(Math.toIntExact(stats.get(5)));
                speedBar.setString("Speed : " + stats.get(5).toString());

                String abilities = imgManager.getCurAbilities();
                abilitiesTextArea.setText(abilities);

                String types = imgManager.getCurTypes();
                typesTextArea.setText(types);

                String pokeName = imgManager.getCurName();
                pokeNameLabel.setText(pokeName);

                pokeNameLabel.setVisible(true);
                pokeStatsLabel.setVisible(true);
                hpBar.setVisible(true);
                attackBar.setVisible(true);
                defenseBar.setVisible(true);
                spAttackBar.setVisible(true);
                spDefenseBar.setVisible(true);
                speedBar.setVisible(true);
                pokeAbilityLabel.setVisible(true);
                abilitiesTextArea.setVisible(true);
                pokeTypeLabel.setVisible(true);
                typesTextArea.setVisible(true);
                pokeLabel.setVisible(true);
            }
        });

        nextBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imgManager.nextImg();
                BufferedImage bufferedImageNext = null;
                try {
                    bufferedImageNext = ImageIO.read(new File("src/images/" + imgManager.getCurImg()));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                Image pokeImg = bufferedImageNext.getScaledInstance(250, 250, Image.SCALE_DEFAULT);
                ImageIcon pokeIcon = new ImageIcon(pokeImg);
                pokeLabel.setIcon(pokeIcon);



                ArrayList<Long> stats = imgManager.getCurrentStats();

                pokeNameLabel.setText(pokeTextField.getText().toUpperCase());
                hpBar.setValue(Math.toIntExact(stats.get(0)));
                hpBar.setString("HP : " + stats.get(0).toString());

                attackBar.setValue(Math.toIntExact(stats.get(1)));
                attackBar.setString("Attack : " + stats.get(1).toString());

                defenseBar.setValue(Math.toIntExact(stats.get(2)));
                defenseBar.setString("Defense : " + stats.get(2).toString());

                spAttackBar.setValue(Math.toIntExact(stats.get(3)));
                spAttackBar.setString("Special Attack : " + stats.get(3).toString());

                spDefenseBar.setValue(Math.toIntExact(stats.get(4)));
                spDefenseBar.setString("Special Defense : " + stats.get(4).toString());

                speedBar.setValue(Math.toIntExact(stats.get(5)));
                speedBar.setString("Speed : " + stats.get(5).toString());

                String abilities = imgManager.getCurAbilities();
                abilitiesTextArea.setText(abilities);

                String types = imgManager.getCurTypes();
                typesTextArea.setText(types);

                String pokeName = imgManager.getCurName();
                pokeNameLabel.setText(pokeName);

                pokeNameLabel.setVisible(true);
                pokeStatsLabel.setVisible(true);
                hpBar.setVisible(true);
                attackBar.setVisible(true);
                defenseBar.setVisible(true);
                spAttackBar.setVisible(true);
                spDefenseBar.setVisible(true);
                speedBar.setVisible(true);
                pokeAbilityLabel.setVisible(true);
                abilitiesTextArea.setVisible(true);
                pokeTypeLabel.setVisible(true);
                typesTextArea.setVisible(true);
                pokeLabel.setVisible(true);
            }
        });
    }

    public static void main(String[] args) throws IOException {
        Pokedex Pokedex = new Pokedex();
        Pokedex.setVisible(true);
    }
}
