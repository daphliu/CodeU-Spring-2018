package codeu.controller;

import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import codeu.model.store.persistence.PersistentDataStoreException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet class responsible for loading test data. */
public class AdminStats extends HttpServlet {

  /** Store class that gives access to Conversations. */
  private ConversationStore conversationStore;

  /** Store class that gives access to Messages. */
  private MessageStore messageStore;

  /** Store class that gives access to Users. */
  private UserStore userStore;

  /** Set up state for handling the load test data request. */
  @Override
  public void init() throws ServletException {
    super.init();
    setConversationStore(ConversationStore.getInstance());
    setMessageStore(MessageStore.getInstance());
    setUserStore(UserStore.getInstance());
  }

  /**
   * Sets the ConversationStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setConversationStore(ConversationStore conversationStore) {
    this.conversationStore = conversationStore;
  }

  /**
   * Sets the MessageStore used by this servlet. This function provides a common setup method for
   * use by the test framework or the servlet's init() function.
   */
  void setMessageStore(MessageStore messageStore) {
    this.messageStore = messageStore;
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  private void loadStats(HttpServletRequest request) {
    int numUsers = userStore.numUsers();
    int numConversations = conversationStore.getNumConversations();
    int numMessages = messageStore.getNumMessages();
    String newestUser = "";
    if (userStore.getLastUser() != null) {
      newestUser = userStore.getLastUser().getName();
    }
    request.setAttribute("numUsers", numUsers);
    request.setAttribute("numConversations", numConversations);
    request.setAttribute("numMessages", numMessages);
    request.setAttribute("newestUser", newestUser);
  }

  /**
   * This function fires when a user requests the /testdata URL. It simply forwards the request to
   * testdata.jsp.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    loadStats(request);
    request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
  }

  /**
   * This function fires when a user submits the testdata form. It loads test data if the user
   * clicked the confirm button.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String changeAdminStatusButton = request.getParameter("change_admin_status");
    String confirmButton = request.getParameter("confirm");
    String username = request.getParameter("username");
    User user = userStore.getUser(username);

    try {

      if (changeAdminStatusButton != null) {
        if (user == null) {
          request.setAttribute("error", "Not a user.");
        } else {
          if (changeAdminStatusButton.equals("promote")) {
            if (user.getAdminStatus()) {
              request.setAttribute("error", "User is already an admin.");
            } else {
              userStore.setIsAdmin(user, true);
              request.setAttribute("success", "Promoted the user to admin!");
            }
          } else {
            if (user.getAdminStatus()) {
              userStore.setIsAdmin(user, false);
              request.setAttribute("success", "Demoted the admin to user :(");
            } else {
              request.setAttribute("error", "User is already not an admin.");
            }
          }
        }
      } else if (confirmButton != null) {
        userStore.loadTestData();
        conversationStore.loadTestData();
        messageStore.loadTestData();

        response.sendRedirect("/");
        return;
      } 

    } catch (PersistentDataStoreException e) {
      request.setAttribute("error", "Internal error");
    }

    loadStats(request);
    request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);    
  }
}
