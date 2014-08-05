package org.spoutcraft.launcher.skin;

import net.technicpack.launchercore.auth.AuthResponse;
import net.technicpack.launchercore.auth.AuthenticationService;
import net.technicpack.launchercore.exception.AuthenticationNetworkFailureException;
import net.technicpack.launchercore.install.user.IAuthListener;
import net.technicpack.launchercore.install.user.User;
import net.technicpack.launchercore.install.user.UserModel;
import net.technicpack.launchercore.install.user.skins.SkinRepository;
import net.technicpack.launchercore.util.ResourceUtils;
import org.spoutcraft.launcher.skin.components.*;

import javax.swing.*;
import javax.swing.plaf.metal.MetalComboBoxUI;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.Collection;
import java.util.Locale;

import static net.technicpack.launchercore.util.ResourceUtils.getResourceAsStream;

public class LoginFrame extends JFrame implements KeyListener, ActionListener, MouseListener, MouseMotionListener, IAuthListener {
    public static final Color CHARCOAL = new Color(45, 45, 45);
    
    private static final URL technicIcon = ErrorDialog.class.getResource("/org/spoutcraft/launcher/resources/icon.png");

    private JLabel nameLabel;
    private JTextField name;
    private JComboBox nameSelect;
    private JLabel passLabel;
    private JPasswordField pass;
    private BlueButton login;
    private JLabel background;
    private JLabel platformImage;
    private JLabel instructionText;
    private JCheckBox rememberAccount;

    private UserCellRenderer userRenderer;
    private UserCellEditor userEditor;

    private HyperlinkJLabel tosLink;
    private JLabel dash;
    private HyperlinkJLabel privacyPolicy;

    private ImageButton closeButton;

    private int dragGripX;
    private int dragGripY;

    private SkinRepository mSkinRepo;
    private UserModel mUserModel;

    private static final int FRAME_WIDTH = 347;
    private static final int FRAME_HEIGHT = 411;

    private static final String CLOSE_ACTION = "close";
    private static final String LOGIN_ACTION = "login";
    private static final String CHANGE_USER = "change_user";
    private static final String TOGGLE_REMEMBER = "remember";

    public LoginFrame(SkinRepository skinRepo, UserModel userModel) {
        mSkinRepo = skinRepo;
        mUserModel = userModel;

        mUserModel.addAuthListener(this);

        //UI Setup
        initComponents();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((dim.width - FRAME_WIDTH) / 2, (dim.height - FRAME_HEIGHT) / 2, FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setBackground(Color.WHITE);
        this.setUndecorated(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(technicIcon));

        //Refresh users from Launcher.getUsers() on initial run
        refreshUsers();
    }

    /**
     * Generate & setup UI components for the frame
     */
    private void initComponents() {
        Font largeFont = getFrameFont(18);
        Font smallFont = getFrameFont(16);
        Font veryLargeFont = getFrameFont(24);
        Font verySmallFont = getFrameFont(14);

        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //Logo at the top
        platformImage = new JLabel();
        LauncherFrame.setIcon(platformImage, "platform_logo.png", 305, 56);
        platformImage.setBounds(21, 21, 305, 56);

        instructionText = new JLabel("Login using your Minecraft credentials");
        instructionText.setFont(verySmallFont);
        instructionText.setBounds(28, 80, FRAME_WIDTH - 50, 30);
        instructionText.setForeground(CHARCOAL);

        nameLabel = new JLabel("Username");
        nameLabel.setFont(largeFont);
        nameLabel.setBounds(25, 110, FRAME_WIDTH - 60, 30);
        nameLabel.setForeground(CHARCOAL);

        // Setup username box
        nameSelect = new JComboBox();

        if (System.getProperty("os.name").toLowerCase(Locale.ENGLISH).contains("mac")) {
            nameSelect.setUI(new MetalComboBoxUI());
        }

        nameSelect.setBounds(25, 140, 297, 32);
        nameSelect.setFont(largeFont);
        nameSelect.setEditable(true);
        nameSelect.setVisible(false);
        userRenderer = new UserCellRenderer(largeFont, mSkinRepo);
        nameSelect.setRenderer(userRenderer);
        userEditor = new UserCellEditor(largeFont, mSkinRepo);
        nameSelect.setEditor(userEditor);
        userEditor.addKeyListener(this);
        nameSelect.addActionListener(this);
        nameSelect.setActionCommand(CHANGE_USER);

        name = new JTextField();
        name.setBounds(25, 140, 297, 30);
        name.setFont(largeFont);
        name.addKeyListener(this);

        passLabel = new JLabel("Password");
        passLabel.setFont(largeFont);
        passLabel.setBounds(25, 175, FRAME_WIDTH - 60, 30);
        passLabel.setForeground(CHARCOAL);

        // Setup password box
        pass = new JPasswordField();
        pass.setBounds(25, 205, 297, 30);
        pass.setFont(largeFont);
        pass.addKeyListener(this);
        pass.setEchoChar('*');
        pass.addActionListener(this);
        pass.setActionCommand(LOGIN_ACTION);

        // "Remember this account"
        rememberAccount = new JCheckBox("Remember this account", false);
        rememberAccount.setFont(smallFont);
        rememberAccount.setForeground(CHARCOAL);
        rememberAccount.setOpaque(false);
        rememberAccount.setBounds(25, 245, 300, 30);
        rememberAccount.setHorizontalTextPosition(SwingConstants.LEFT);
        rememberAccount.setHorizontalAlignment(SwingConstants.RIGHT);
        rememberAccount.setIconTextGap(12);
        rememberAccount.addActionListener(this);
        rememberAccount.setActionCommand(TOGGLE_REMEMBER);
        rememberAccount.addKeyListener(this);

        //Login button
        login = new BlueButton("LOGIN");
        login.setFont(veryLargeFont);
        login.setBounds(25, 290, FRAME_WIDTH - 50, 40);
        login.setActionCommand(LOGIN_ACTION);
        login.addActionListener(this);

        // Dash
        dash = new JLabel("-");
        dash.setFont(verySmallFont);
        dash.setForeground(CHARCOAL);
        dash.setBounds((FRAME_WIDTH / 2) - 2, 375, 20, 20);

        //Terms of Service
        tosLink = new HyperlinkJLabel("Terms of Service", "http://www.technicpack.net/terms");
        tosLink.setFont(verySmallFont);
        tosLink.setForeground(CHARCOAL);
        tosLink.setBounds(dash.getX() - 115, dash.getY(), 115, 20);
        tosLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        //Privacy Policy
        privacyPolicy = new HyperlinkJLabel("Privacy Policy", "http://www.technicpack.net/privacy");
        privacyPolicy.setFont(verySmallFont);
        privacyPolicy.setForeground(CHARCOAL);
        privacyPolicy.setBounds(dash.getX() + 10, dash.getY(), 105, 20);
        privacyPolicy.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        //Close button
        closeButton = new ImageButton(ResourceUtils.getIcon("login_close.png"));
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeButton.setBounds(FRAME_WIDTH - 28, 8, 20, 21);
        closeButton.setActionCommand(CLOSE_ACTION);
        closeButton.addActionListener(this);

        background = new JLabel();
        background.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        LauncherFrame.setIcon(background, "login_background.png", background.getWidth(), background.getHeight());

        contentPane.add(closeButton);
        contentPane.add(tosLink);
        contentPane.add(privacyPolicy);
        contentPane.add(dash);
        contentPane.add(login);
        contentPane.add(rememberAccount);
        contentPane.add(instructionText);
        contentPane.add(nameLabel);
        contentPane.add(passLabel);
        contentPane.add(name);
        contentPane.add(nameSelect);
        contentPane.add(pass);
        contentPane.add(platformImage);
        contentPane.add(background);
    }

    /**
     * Refreshes the login UI - pulls the latest list of users from Launcher.getUsers(), sets up the combo box with the
     * user list if a list of users is available, or otherwise sets up a blank username text field.  Will attempt to
     * select the last-logged-in user (or first in the list) if available.
     */
    private void refreshUsers() {
        Collection<User> userAccounts = mUserModel.getUsers();
        User lastUser = mUserModel.getLastUser();

        if (userAccounts.size() == 0) {
            name.setVisible(true);
            nameSelect.setVisible(false);
            clearCurrentUser();
        } else {
            name.setVisible(false);
            nameSelect.setVisible(true);
            nameSelect.removeAllItems();

            for (User account : userAccounts) {
                nameSelect.addItem(account);
            }

            nameSelect.addItem(null);

            if (lastUser == null)
                lastUser = userAccounts.iterator().next();

            setCurrentUser(lastUser);
        }
    }

    /**
     * Preps the UI to enter a brand new user.
     */
    private void clearCurrentUser() {
        pass.setText("");
        pass.setEditable(true);
        pass.setBackground(Color.WHITE);
        rememberAccount.setSelected(false);

        name.setText("");
        nameSelect.setSelectedItem("");
    }

    /**
     * Clear a user from the internal user database & refresh the UI
     *
     * @param user The user to remove
     */
    private void forgetUser(User user) {
        mUserModel.removeUser(user);
        refreshUsers();
    }

    /**
     * Preps the UI to show an existing user we have credentials for
     *
     * @param user The user object to show in the dropdown, etc.
     */
    private void setCurrentUser(User user) {
        if (user == null) {
            clearCurrentUser();
            return;
        }

        pass.setText("PASSWORD");
        pass.setEditable(false);
        pass.setBackground(Color.lightGray);
        rememberAccount.setSelected(true);

        nameSelect.setSelectedItem(user);
    }

    /**
     * Preps the UI to show a pre-setup login form for an existing username.  We use this to set up a form after
     * a user session that we previously had saved expires- they should just have to type in their password & hit
     * enter.
     *
     * @param username Username of the user being set up to sign in.
     */
    private void setCurrentUser(String username) {
        if (this.name.isVisible())
            this.name.setText(username);
        else
            this.nameSelect.setSelectedItem(username);

        pass.setText("");
        pass.setEditable(true);
        pass.setBackground(Color.WHITE);
        rememberAccount.setSelected(true);
    }

    /**
     * Retrieve the Raleway font in the specified pt Size
     *
     * @param size Pt size of requested font
     * @return Font object to be used for UI elements
     */
    private Font getFrameFont(int size) {
        Font font;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, getResourceAsStream("/org/spoutcraft/launcher/resources/Raleway-SemiBold.ttf")).deriveFont((float) size);
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback
            font = new Font("Arial", Font.PLAIN, 12);
        }
        return font;
    }

    /**
     * Given the current UI state, attempt to log in to the minecraft auth service & activate the launcher frame.
     */
    private void attemptLogin() {
        if (nameSelect.isVisible()) {
            Object selected = nameSelect.getSelectedItem();

            if (selected instanceof User) {
                verifyExistingLogin((User) selected);
            } else {
                String username = selected.toString();

                User user = mUserModel.getUser(username);

                if (user == null)
                    attemptNewLogin(username);
                else {
                    setCurrentUser(user);
                    verifyExistingLogin(user);
                }
            }
        } else {
            attemptNewLogin(name.getText());
        }
    }

    /**
     * Attempt to verify & refresh the session of an already-logged in user.  If the Auth service is unavailable,
     * prompt the user for Offline Play.  If the verify/refresh is successful, or Offline Play is accepted, then
     * activate the launcher frame.
     *
     * @param user The already-logged in user to verify & refresh the session for.
     * @return True if the launcher frame was successfully activated, false otherwise
     */
    private boolean verifyExistingLogin(User user) {
        User loginUser = user;
        boolean rejected = false;

        try {
            UserModel.AuthError error = mUserModel.AttemptUserRefresh(user);
            if (error != null) {
                JOptionPane.showMessageDialog(this, error.getErrorDescription(), error.getError(), JOptionPane.ERROR_MESSAGE);
                loginUser = null;
                rejected = true;
            }
        } catch (AuthenticationNetworkFailureException ex) {
            ex.printStackTrace();

            //Couldn't reach auth server- if we're running silently (we just started up and have a user session ready to roll)
            //Go ahead and just play offline automatically, like the minecraft client does.  If we're running loud (user
            //is actually at the login UI clicking the login button), give them a choice.
            if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this,
                    "The auth servers at Minecraft.net are inaccessible.  Would you like to play offline?",
                    "Offline Play", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)) {

                //This is the last time we'll have access to the user's real username, so we should set the last-used
                //username now
                mUserModel.setLastUser(user);

                //Create offline user
                loginUser = new User(user.getDisplayName());
            } else {
                //Use clicked 'no', so just pull the ripcord and get back to the UI
                loginUser = null;
            }
        }

        if (loginUser == null) {
            //If we actually failed to validate, we should remove the user from the list of saved users
            //and refresh the user list
            if (rejected) {
                mUserModel.removeUser(user);
                refreshUsers();
                setCurrentUser(user.getUsername());
            }

            return false;
        }


        return true;
    }

    /**
     * Attempt to authenticate a user with the Mojang servers, with the given username and the password in the UI. If
     * successful, activate the launcher frame.
     *
     * @param username The username to auth with Mojang as.
     */
    private void attemptNewLogin(String username) {

        AuthResponse response = null;
        try {
            //Attempt the log the user in with the data from this form
            response = AuthenticationService.requestLogin(username, new String(this.pass.getPassword()), mUserModel.getClientToken());

            if (response == null) {
                JOptionPane.showMessageDialog(this, "Invalid credentials. Invalid username or password.", "Auth Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (response.getError() != null) {
                JOptionPane.showMessageDialog(this, response.getErrorMessage(), response.getError(), JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (AuthenticationNetworkFailureException ex) {
            //Login servers are inaccessible, but we only give the option to play offline with pre-cached users
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while attempting to reach Minecraft.net", "Auth Servers Inaccessible", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Create an online user with the received data
        User clearedUser = new User(username, response);

        if (rememberAccount.isSelected()) {
            //Add user to our list of cached users if checkbox is true
            mUserModel.addUser(clearedUser);
        }

        mUserModel.setCurrentUser(clearedUser);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == rememberAccount && e.getKeyCode() == KeyEvent.VK_ENTER) {
            attemptLogin();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JComponent) {
            String command = e.getActionCommand();
            JComponent source = (JComponent) e.getSource();

            if (command.equals(CLOSE_ACTION)) {
                System.exit(0);
            } else if (command.equals(LOGIN_ACTION)) {
                attemptLogin();
            } else if (command.equals(CHANGE_USER)) {
                if (nameSelect.getSelectedItem() == null || nameSelect.getSelectedItem().equals("")) {
                    clearCurrentUser();
                } else if (nameSelect.getSelectedItem() instanceof User) {
                    setCurrentUser((User) nameSelect.getSelectedItem());
                }
            } else if (command.equals(TOGGLE_REMEMBER)) {
                if (!rememberAccount.isSelected() && nameSelect.isVisible() && nameSelect.getSelectedItem() instanceof User) {
                    forgetUser((User) nameSelect.getSelectedItem());
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //None
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (e.getButton() == MouseEvent.BUTTON1) {
            dragGripX = e.getX();
            dragGripY = e.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // None
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // None
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // None
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if ((e.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) != 0) {
            this.setLocation(e.getXOnScreen() - dragGripX, e.getYOnScreen() - dragGripY);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // None
    }

    @Override
    public void userChanged(User user) {
        if (user == null) {
            this.setVisible(true);
            refreshUsers();
        } else
            this.setVisible(false);
    }
}
