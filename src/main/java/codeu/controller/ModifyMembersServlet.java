package codeu.controller;
import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.io.*;

public class ModifyMembersServlet extends HttpServlet {
  /** Store class that gives access to Conversations. */
  private ConversationStore conversationStore;

  /** Store class that gives access to Messages. */
  private MessageStore messageStore;

  /** Store class that gives access to Users. */
  private UserStore userStore;

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
  @Override
  public void init() throws ServletException {
    super.init();
    setConversationStore(ConversationStore.getInstance());
    setMessageStore(MessageStore.getInstance());
    setUserStore(UserStore.getInstance());
  }

 @Override
 public void doGet(HttpServletRequest request, HttpServletResponse response)
     throws IOException, ServletException {
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
  throws IOException, ServletException
  {
    //Conversation curr = (Conversation)request.getAttribute("conversation");
    //String tryT = curr.getTitle();
    //System.out.println("is this name? " + tryT);
    //TODO: redirect if user is not member of conversaoin
      //  Conversation conversation = conversationStore.getConversationWithTitle(conversationTitle);
        //String uri = request.getRequestURI();
        //String boolAppera = request.getParameter("onclick");
        //  System.out.println(boolAppera);
        //System.out.println(uri);
        System.out.println("HELLO");
        String conversationTitle= request.getParameter("chatTitle");
        Conversation conversation = conversationStore.getConversationWithTitle(conversationTitle);
        System.out.println(conversationTitle);
        String sV = request.getParameter("remove");
        String addM = request.getParameter("addMbr");

        //load conversation & remove user from list
        System.out.println(sV);
        if (addM!=null){ //means addM was pushed
          try{
            conversationStore.removeConversationFromInStoreList(conversation);
            conversationStore.addMemberinPD(conversation.getId(), UUID.fromString(addM));
            Conversation newConvo = conversationStore.getConversationFromPD(conversation.getId());

            conversationStore.addConversation(newConvo);
          } catch(Exception e){
            System.out.println("in modify members catch stmetn");
          }
        }
        if (sV!=null){ //means remove btn was pushed


        }
        System.out.println(addM);

        response.sendRedirect("/chat/" + conversationTitle);
  }

}
