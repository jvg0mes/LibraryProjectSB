package com.project.library.Models;

public class CurrentSession {
    private static Session session = null;

    public static Session getSession() {
        return session;
    }
    public static void setSession(Session session){
        CurrentSession.session = session;
        System.out.println("Sessao iniciada com sucesso:"+getSession());
    }
    public static void logout(){
        CurrentSession.setSession(null);
    }

}
