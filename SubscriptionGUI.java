import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.*;

/**
 * SubscriptionGUI - Graphical User Interface for the AI Subscription Management System.
 * Stores an ArrayList of AIModel objects and provides buttons to manage plans.
 *
 * @author Krimon Gurung
 * @version 2.0 (Milestone 2)
 */
public class SubscriptionGUI extends JFrame implements ActionListener {

    // Data
    private ArrayList<AIModel> plans = new ArrayList<>();

    // Colours (dark navy — matches professional GUI standard)
    private static final Color BG_DARK  = new Color(24,  44,  74);
    private static final Color BG_MID   = new Color(30,  55,  90);
    private static final Color FG_LABEL = new Color(200, 215, 235);
    private static final Color BTN_BG   = new Color(220, 225, 232);
    private static final Color BTN_FG   = new Color(20,  40,  70);

    // Plan Details fields 
    private JTextField tfModelName  = mkField(18);
    private JTextField tfPrice      = mkField(12);
    private JTextField tfParams     = mkField(12);
    private JTextField tfContext    = mkField(12);
    private JTextField tfQuota      = mkField(12);
    private JTextField tfSlots      = mkField(12);

    // Date-style dropdowns for Membership Start Date (cosmetic, like Gym GUI)
    private JComboBox<String> cbDay   = new JComboBox<>();
    private JComboBox<String> cbMonth = new JComboBox<>();
    private JComboBox<String> cbYear  = new JComboBox<>();

    // Plan type selector
    private JComboBox<String> cbPlanType = new JComboBox<>(new String[]{"Personal Plan","Pro Plan"});

    // ── Operation fields ─────────────────────────────────────────────────────
    private JTextField tfIndex      = mkField(8);
    private JTextField tfPrompt     = mkField(22);
    private JTextField tfOutLen     = mkField(10);
    private JTextField tfMember     = mkField(18);
    private JTextField tfPurchase   = mkField(10);

    // ── Output ───────────────────────────────────────────────────────────────
    private JTextArea taOut = new JTextArea(11, 58);

    // ── Buttons (3 rows of 5 — identical layout to Gym Management System) ────
    private JButton btnAddPersonal  = mkBtn("Add Personal Plan");
    private JButton btnAddPro       = mkBtn("Add Pro Plan");
    private JButton btnUsePrompt    = mkBtn("Use Prompt");
    private JButton btnAddMember    = mkBtn("Add Team Member");
    private JButton btnMarkAttend   = mkBtn("Mark Attendance");
    private JButton btnRevertPers   = mkBtn("Revert Personal");
    private JButton btnRevertPro    = mkBtn("Revert Pro");
    private JButton btnRemoveMember = mkBtn("Remove Team Member");
    private JButton btnPurchase     = mkBtn("Purchase Prompts");
    private JButton btnClear        = mkBtn("Clear");
    private JButton btnCheckType    = mkBtn("Check Plan Type");
    private JButton btnLoad         = mkBtn("Load from File");
    private JButton btnSave         = mkBtn("Save to File");
    private JButton btnDisplay      = mkBtn("Display");

    // Constructor
    public SubscriptionGUI() {
        setTitle("AI Subscription Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(BG_DARK);

        for (int d=1;d<=31;d++) cbDay.addItem(String.valueOf(d));
        for (int m=1;m<=12;m++) cbMonth.addItem(String.valueOf(m));
        for (int y=2024;y<=2030;y++) cbYear.addItem(String.valueOf(y));
        for (JComboBox<?> cb:new JComboBox[]{cbDay,cbMonth,cbYear,cbPlanType}) styleCombo(cb);

        JPanel root = new JPanel(new BorderLayout(0,0));
        root.setBackground(BG_DARK);
        root.add(buildTitle(),  BorderLayout.NORTH);
        root.add(buildCentre(), BorderLayout.CENTER);
        root.add(buildSouth(),  BorderLayout.SOUTH);

        for (JButton b:new JButton[]{btnAddPersonal,btnAddPro,btnUsePrompt,
                btnAddMember,btnMarkAttend,btnRevertPers,btnRevertPro,
                btnRemoveMember,btnPurchase,btnClear,btnCheckType,
                btnLoad,btnSave,btnDisplay}) b.addActionListener(this);

        add(root);
        pack();
        setMinimumSize(new Dimension(1120,800));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // UI builders 
    private JLabel buildTitle() {
        JLabel t=new JLabel("Welcome to the AI Subscription Management System",SwingConstants.CENTER);
        t.setFont(new Font("Arial",Font.BOLD,21));
        t.setForeground(Color.WHITE);
        t.setOpaque(true); t.setBackground(BG_DARK);
        t.setBorder(new EmptyBorder(22,10,14,10));
        return t;
    }

    private JPanel buildCentre() {
        JPanel p=new JPanel(new GridLayout(1,2,18,0));
        p.setBackground(BG_DARK);
        p.setBorder(new EmptyBorder(0,18,10,18));
        p.add(buildLeft());
        p.add(buildRight());
        return p;
    }

    private JPanel buildLeft() {
        JPanel p=titled("  Plan Details  ");
        p.setLayout(new GridBagLayout());
        GridBagConstraints lc=lGbc(), fc=fGbc();
        Object[][] rows={
            {"Model Name:",         tfModelName},
            {"Price (NPR/1L tok):", tfPrice},
            {"Parameters (B):",     tfParams},
            {"Context Window:",     tfContext},
            {"Plan Type:",          cbPlanType},
            {"Prompt Quota:",       tfQuota},
            {"Team Slots:",         tfSlots},
            {"Purchase Amount:",    tfPurchase},
        };
        int r=0;
        for(Object[] row:rows){lc.gridy=r;fc.gridy=r;p.add(lbl((String)row[0]),lc);p.add((Component)row[1],fc);r++;}
        lc.gridy=r; fc.gridy=r;
        p.add(lbl("Start Date:"),lc);
        JPanel dp=new JPanel(new FlowLayout(FlowLayout.LEFT,4,0)); dp.setBackground(BG_MID);
        dp.add(cbDay); dp.add(cbMonth); dp.add(cbYear);
        p.add(dp,fc);
        return p;
    }

    private JPanel buildRight() {
        JPanel outer=new JPanel(new GridLayout(2,1,0,14)); outer.setBackground(BG_DARK);

        JPanel top=titled("  Prompt Operations  ");
        top.setLayout(new GridBagLayout());
        GridBagConstraints lc=lGbc(),fc=fGbc();
        Object[][] tr={{"Index Number:",tfIndex},{"Prompt Text:",tfPrompt},{"Output Length (tokens):",tfOutLen}};
        int r=0;for(Object[]row:tr){lc.gridy=r;fc.gridy=r;top.add(lbl((String)row[0]),lc);top.add((Component)row[1],fc);r++;}

        JPanel bot=titled("  Team Operations  ");
        bot.setLayout(new GridBagLayout());
        GridBagConstraints lc2=lGbc(),fc2=fGbc();
        lc2.gridy=0;fc2.gridy=0;
        bot.add(lbl("Team Member Name:"),lc2); bot.add(tfMember,fc2);

        outer.add(top); outer.add(bot);
        return outer;
    }

    private JPanel buildSouth() {
        JPanel p=new JPanel(new BorderLayout(0,8)); p.setBackground(BG_DARK);
        p.setBorder(new EmptyBorder(0,18,18,18));
        p.add(buildButtons(),BorderLayout.NORTH);
        p.add(buildOutput(), BorderLayout.CENTER);
        return p;
    }

    private JPanel buildButtons() {
        JLabel lbl=new JLabel("  Action:"); lbl.setFont(new Font("Arial",Font.BOLD,14)); lbl.setForeground(Color.WHITE);
        JPanel grid=new JPanel(new GridLayout(3,5,8,6)); grid.setBackground(BG_DARK);
        // Row 1
        grid.add(btnAddPersonal); grid.add(btnAddPro);      grid.add(btnUsePrompt);    grid.add(btnAddMember);    grid.add(btnMarkAttend);
        // Row 2
        grid.add(btnRevertPers);  grid.add(btnRevertPro);   grid.add(btnRemoveMember); grid.add(btnPurchase);     grid.add(btnClear);
        // Row 3
        grid.add(btnCheckType);   grid.add(btnLoad);        grid.add(btnSave);         grid.add(btnDisplay);      grid.add(new JLabel());
        JPanel w=new JPanel(new BorderLayout(0,4)); w.setBackground(BG_DARK);
        w.add(lbl,BorderLayout.NORTH); w.add(grid,BorderLayout.CENTER);
        return w;
    }

    private JPanel buildOutput() {
        taOut.setEditable(false);
        taOut.setFont(new Font("Monospaced",Font.PLAIN,12));
        taOut.setBackground(new Color(15,30,55));
        taOut.setForeground(new Color(180,210,240));
        taOut.setBorder(new EmptyBorder(8,10,8,10));
        JScrollPane sp=new JScrollPane(taOut);
        sp.setBorder(BorderFactory.createTitledBorder(new LineBorder(new Color(80,110,150),1),
                "  Output  ",TitledBorder.LEFT,TitledBorder.TOP,new Font("Arial",Font.BOLD,12),Color.WHITE));
        JPanel p=new JPanel(new BorderLayout()); p.setBackground(BG_DARK); p.add(sp); return p;
    }

    //Index validation — returns -1 on any error 
    private int getDisplayNumber() {
        int idx=-1;
        try {
            idx=Integer.parseInt(tfIndex.getText().trim());
            if(idx<0||idx>=plans.size()){
                JOptionPane.showMessageDialog(this,
                    "Index out of range. Valid range: 0 to "+(plans.size()-1),
                    "Index Error",JOptionPane.ERROR_MESSAGE);
                return -1;
            }
        } catch(NumberFormatException e){
            JOptionPane.showMessageDialog(this,
                "Invalid index. Please enter a whole number.",
                "Input Error",JOptionPane.ERROR_MESSAGE);
            return -1;
        }
        return idx;
    }

    // Check plan type using instanceof
    private void checkPlanType(int i) {
        AIModel m=plans.get(i);
        String msg=(m instanceof PersonalPlan)?"Index "+i+" is a Personal Plan."
                  :(m instanceof ProPlan)     ?"Index "+i+" is a Pro Plan."
                  :"Unknown plan type at index "+i;
        JOptionPane.showMessageDialog(this,msg,"Plan Type",JOptionPane.INFORMATION_MESSAGE);
    }

    // Button handler
    @Override
    public void actionPerformed(ActionEvent e) {
        Object s=e.getSource();

        if(s==btnAddPersonal){
            try{
                PersonalPlan pp=new PersonalPlan(tfModelName.getText().trim(),
                    Double.parseDouble(tfPrice.getText().trim()),
                    Integer.parseInt(tfParams.getText().trim()),
                    Integer.parseInt(tfContext.getText().trim()),
                    Integer.parseInt(tfQuota.getText().trim()));
                plans.add(pp);
                taOut.append("Added Personal Plan [Index "+(plans.size()-1)+"]:\n"+pp.display()+"\n\n");
            }catch(NumberFormatException ex){
                JOptionPane.showMessageDialog(this,"Please enter valid numeric values.","Input Error",JOptionPane.ERROR_MESSAGE);
            }

        }else if(s==btnAddPro){
            try{
                ProPlan pro=new ProPlan(tfModelName.getText().trim(),
                    Double.parseDouble(tfPrice.getText().trim()),
                    Integer.parseInt(tfParams.getText().trim()),
                    Integer.parseInt(tfContext.getText().trim()),
                    Integer.parseInt(tfSlots.getText().trim()));
                plans.add(pro);
                taOut.append("Added Pro Plan [Index "+(plans.size()-1)+"]:\n"+pro.display()+"\n\n");
            }catch(NumberFormatException ex){
                JOptionPane.showMessageDialog(this,"Please enter valid numeric values.","Input Error",JOptionPane.ERROR_MESSAGE);
            }

        }else if(s==btnUsePrompt){
            int i=getDisplayNumber();
            if(i!=-1){
                try{
                    // Polymorphic call — no instanceof needed
                    String res=plans.get(i).usePrompt(tfPrompt.getText().trim(),
                                Integer.parseInt(tfOutLen.getText().trim()));
                    taOut.append(res+"\n\n");
                }catch(NumberFormatException ex){
                    JOptionPane.showMessageDialog(this,"Output length must be a whole number.","Input Error",JOptionPane.ERROR_MESSAGE);
                }
            }

        }else if(s==btnAddMember){
            int i=getDisplayNumber();
            if(i!=-1){
                AIModel m=plans.get(i);
                if(m instanceof ProPlan) taOut.append(((ProPlan)m).addTeamMember(tfMember.getText().trim())+"\n\n");
                else JOptionPane.showMessageDialog(this,"Team collaboration is only available for Pro Plan subscriptions.","Plan Error",JOptionPane.ERROR_MESSAGE);
            }

        }else if(s==btnRemoveMember){
            int i=getDisplayNumber();
            if(i!=-1){
                AIModel m=plans.get(i);
                if(m instanceof ProPlan) taOut.append(((ProPlan)m).removeTeamMember(tfMember.getText().trim())+"\n\n");
                else JOptionPane.showMessageDialog(this,"Remove Team Member is only for Pro Plan.","Plan Error",JOptionPane.ERROR_MESSAGE);
            }

        }else if(s==btnPurchase){
            int i=getDisplayNumber();
            if(i!=-1){
                AIModel m=plans.get(i);
                if(m instanceof PersonalPlan){
                    try{
                        taOut.append(((PersonalPlan)m).purchasePrompts(Integer.parseInt(tfPurchase.getText().trim()))+"\n\n");
                    }catch(NumberFormatException ex){
                        JOptionPane.showMessageDialog(this,"Purchase amount must be a whole number.","Input Error",JOptionPane.ERROR_MESSAGE);
                    }
                }else JOptionPane.showMessageDialog(this,"Purchase Prompts is only for Personal Plan.","Plan Error",JOptionPane.ERROR_MESSAGE);
            }

        }else if(s==btnMarkAttend){
            int i=getDisplayNumber();
            if(i!=-1) taOut.append("Attendance marked for [Index "+i+"] "+plans.get(i).getModelName()+".\n\n");

        }else if(s==btnRevertPers){
            if(!plans.isEmpty()&&plans.get(plans.size()-1) instanceof PersonalPlan){
                plans.remove(plans.size()-1); taOut.append("Last Personal Plan removed.\n\n");
            }else JOptionPane.showMessageDialog(this,"Last plan is not a Personal Plan.","Revert Error",JOptionPane.ERROR_MESSAGE);

        }else if(s==btnRevertPro){
            if(!plans.isEmpty()&&plans.get(plans.size()-1) instanceof ProPlan){
                plans.remove(plans.size()-1); taOut.append("Last Pro Plan removed.\n\n");
            }else JOptionPane.showMessageDialog(this,"Last plan is not a Pro Plan.","Revert Error",JOptionPane.ERROR_MESSAGE);

        }else if(s==btnCheckType){
            int i=getDisplayNumber(); if(i!=-1) checkPlanType(i);

        }else if(s==btnDisplay){
            if(plans.isEmpty()){taOut.append("No plans available.\n\n");return;}
            StringBuilder sb=new StringBuilder("===== All Plans =====\n");
            for(int i=0;i<plans.size();i++) sb.append("Index "+i+":\n"+plans.get(i).display()+"\n\n");
            taOut.append(sb.toString());

        }else if(s==btnSave){
            try(ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream("subscription_data.dat"))){
                oos.writeObject(plans);
                taOut.append("Data saved to subscription_data.dat successfully.\n\n");
            }catch(IOException ex){JOptionPane.showMessageDialog(this,"Error saving: "+ex.getMessage(),"File Error",JOptionPane.ERROR_MESSAGE);}

        }else if(s==btnLoad){
            try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream("subscription_data.dat"))){
                plans=(ArrayList<AIModel>)ois.readObject();
                taOut.append("Loaded "+plans.size()+" plan(s):\n");
                for(int i=0;i<plans.size();i++) taOut.append("Index "+i+":\n"+plans.get(i).display()+"\n\n");
            }catch(IOException|ClassNotFoundException ex){JOptionPane.showMessageDialog(this,"Error loading: "+ex.getMessage(),"File Error",JOptionPane.ERROR_MESSAGE);}

        }else if(s==btnClear){
            for(JTextField tf:new JTextField[]{tfModelName,tfPrice,tfParams,tfContext,tfQuota,tfSlots,tfIndex,tfPrompt,tfOutLen,tfMember,tfPurchase}) tf.setText("");
            taOut.setText("");
        }
    }

    // Helpers
    private static JTextField mkField(int cols){
        JTextField tf=new JTextField(cols);
        tf.setBackground(new Color(200,215,235)); tf.setForeground(new Color(20,40,70));
        tf.setFont(new Font("Arial",Font.PLAIN,13));
        tf.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(100,130,170),1),new EmptyBorder(4,6,4,6)));
        return tf;
    }
    private JLabel lbl(String t){JLabel l=new JLabel(t);l.setForeground(FG_LABEL);l.setFont(new Font("Arial",Font.PLAIN,13));return l;}
    private JPanel titled(String t){
        JPanel p=new JPanel(); p.setBackground(BG_MID);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(new LineBorder(new Color(80,110,150),1),t,TitledBorder.LEFT,TitledBorder.TOP,new Font("Arial",Font.BOLD,13),Color.WHITE),
            new EmptyBorder(10,14,10,14)));
        return p;
    }
    private void styleCombo(JComboBox<?> cb){cb.setBackground(new Color(200,215,235));cb.setForeground(new Color(20,40,70));cb.setFont(new Font("Arial",Font.PLAIN,13));}
    private JButton mkBtn(String t){
        JButton b=new JButton(t); b.setBackground(BTN_BG); b.setForeground(BTN_FG);
        b.setFont(new Font("Arial",Font.BOLD,12)); b.setFocusPainted(false);
        b.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(130,150,180),1),new EmptyBorder(7,8,7,8)));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR)); return b;
    }
    private GridBagConstraints lGbc(){GridBagConstraints g=new GridBagConstraints();g.gridx=0;g.insets=new Insets(6,4,6,8);g.anchor=GridBagConstraints.WEST;return g;}
    private GridBagConstraints fGbc(){GridBagConstraints g=new GridBagConstraints();g.gridx=1;g.insets=new Insets(6,0,6,4);g.fill=GridBagConstraints.HORIZONTAL;g.weightx=1.0;return g;}

    public static void main(String[] args) {
        try{UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}catch(Exception ignored){}
        SwingUtilities.invokeLater(SubscriptionGUI::new);
    }
}