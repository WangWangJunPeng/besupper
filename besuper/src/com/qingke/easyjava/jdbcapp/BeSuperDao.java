package com.qingke.easyjava.jdbcapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.qingke.easyjava.jdbcapp.pojo.Answer;
import com.qingke.easyjava.jdbcapp.pojo.Player;
import com.qingke.easyjava.jdbcapp.pojo.PlayerCredential;
import com.qingke.easyjava.jdbcapp.pojo.Question;

public class BeSuperDao {

    public BeSuperDao() {
    }
    
    public Player signup(PlayerCredential player) {
//        String sql = "INSERT INTO player VALUES(\"name\",\"username\",\"password\") VALUE (?,?,?)";
        String sql = "INSERT INTO `qingke`.`player` (`name`, `username`, `password`) VALUES (?,?,?)";
        
        Connection conn = null;
        PreparedStatement statement = null;

        try {
            conn = getConnection();
            statement = conn.prepareStatement(sql);

            statement.setString(1, player.getName());
            statement.setString(2, player.getUsername());
            statement.setString(3, player.getPassword());
            
            int affectRows = statement.executeUpdate();
            
            if (affectRows > 0) {
                return login(player);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cleanup(conn, statement, null);
        }

        return null;
    }
    
    public Player login(PlayerCredential credential) {
        String sql = "SELECT id, name, score FROM player WHERE username = ? AND password = ?";
        
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            statement = conn.prepareStatement(sql);
            
            statement.setString(1, credential.getUsername());//第一个问号,传参
            statement.setString(2, credential.getPassword());//第二个问号,
            
            rs = statement.executeQuery();
            
            Player player = null;
            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                int score = rs.getInt("score");
                player = new Player(id, name, score);
            }
            return player;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            cleanup(conn, statement, rs);
        }
    }

    public Player getPlayer(long playerId) {
        String sql = "SELECT id, name, score FROM player WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        Player player = null;
        try {
            conn = getConnection();
            statement = conn.prepareStatement(sql);
            
            statement.setLong(1, playerId);
            
            rs = statement.executeQuery();//执行select语句
            
            while(rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                int score = rs.getInt("score");
                
                player = new Player(id, name, score);
            }
            return player;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cleanup(conn, statement, rs);
        }
        
        return null;
    }

    public void updatePlayer(Player player) {
        String sql = "UPDATE `qingke`.`player` SET `name`=?, `score`=? WHERE `id`=?";
        
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            statement = conn.prepareStatement(sql);
            
            statement.setString(1, player.getName());
            statement.setInt(2, player.getScore());
            statement.setLong(3, player.getId());
            
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            cleanup(conn, statement, rs);
        }
    }

    public List<Question> getQuestionsFrom(Player player) {
        List<Question> questions = new ArrayList<Question>();
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        String sql = "SELECT id, value, credit, isOpen FROM question where player_id = ?";
        try {
            conn = getConnection();
            statement = conn.prepareStatement(sql);
            
            statement.setLong(1, player.getId());

            rs = statement.executeQuery();
            
            while (rs.next()) {
                long id = rs.getLong("id");
                String value =rs.getString("value");
                int credit = rs.getInt("credit");
                boolean isOpen = "Y".equals(rs.getString("isOpen"));
                
                Question question = new Question(id, value, player, credit);
                List<Answer> answers = this.getAnswersFrom(question);
                question.setAnswers(answers);
                question.setOpen(isOpen);

                questions.add(question);
            }

            return questions;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cleanup(conn, statement, rs);
        }
        return questions;
    }

    public List<Question> getQuestions() {
        List<Question> questions = new ArrayList<Question>();
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        String sql = "SELECT id, value, credit, player_id, isOpen FROM question order by isOpen";
        try {
            conn = getConnection();
            statement = conn.prepareStatement(sql);

            rs = statement.executeQuery();
            while (rs.next()) {
                long id = rs.getLong("id");
                String value =rs.getString("value");
                int credit = rs.getInt("credit");
                long playerId = rs.getLong("player_id");
                boolean isOpen = "Y".equals(rs.getString("isOpen"));
                
                Player player = this.getPlayer(playerId);
                
                Question question = new Question(id, value, player, credit);
                question.setOpen(isOpen);

                List<Answer> answers = this.getAnswersFrom(question);
                question.setAnswers(answers);

                questions.add(question);
            }

            return questions;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cleanup(conn, statement, rs);
        }
        return questions;
    }

    public List<Answer> getAnswersFrom(Player player) {
        List<Answer> answers = new ArrayList<Answer>();
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        String sql = "SELECT id, value, best, question_id FROM answer where player_id = ?";
        try {
            conn = getConnection();
            statement = conn.prepareStatement(sql);
            
            statement.setLong(1, player.getId());

            rs = statement.executeQuery();
            while (rs.next()) {
                long id = rs.getLong("id");
                String value = rs.getString("value");
                boolean isBest = "Y".equals(rs.getString("best"));
                long questionId = rs.getLong("question_id");

                Answer answer = new Answer(id, value, player);
                answer.setBestAnswer(isBest);
                answer.setQuestionId(questionId);

                answers.add(answer);
            }
            
            return answers;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cleanup(conn, statement, rs);
        }
        return answers;
    }

    public List<Answer> getAnswersFrom(Question question) {
        List<Answer> answers = new ArrayList<Answer>();
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        String sql = "SELECT id, value, best, player_id FROM answer where question_id = ?";
        try {
            conn = getConnection();
            statement = conn.prepareStatement(sql);
            
            statement.setLong(1, question.getId());

            rs = statement.executeQuery();
            while (rs.next()) {
                long id = rs.getLong("id");
                String value = rs.getString("value");
                boolean isBest = "Y".equals(rs.getString("best"));
                long playerId = rs.getLong("player_id");

                Player player = this.getPlayer(playerId);
                Answer answer = new Answer(id, value, player);
                answer.setBestAnswer(isBest);
                answer.setQuestionId(question.getId());

                answers.add(answer);
            }
            
            return answers;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cleanup(conn, statement, rs);
        }
        return answers;
    }

    public Question getQuestion(long questionId) {
        Question question = null;
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        String sql = "SELECT id, value, player_id, isOpen, credit FROM question where id = ?";
        try {
            conn = getConnection();
            statement = conn.prepareStatement(sql);
            
            statement.setLong(1, questionId);
            
            rs = statement.executeQuery();
            
            while(rs.next()) {
                long id = rs.getInt("id");
                String value = rs.getString("value");
                long playerId = rs.getLong("player_id");
                int credit = rs.getInt("credit");
                boolean isOpen = "Y".equals(rs.getString("isOpen"));
                
                Player player = this.getPlayer(playerId);
                question = new Question(id, value, player, credit);
                question.setOpen(isOpen);

                List<Answer> answers = this.getAnswersFrom(question);
                question.setAnswers(answers);
            }

            return question;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cleanup(conn, statement, rs);
        }
        return question;
    }
    
    public Answer getAnswer(long answerId) {
        Answer answer = null;
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        String sql = "SELECT id, value, best, player_id, question_id from answer where id = ?";
        try {
            conn = getConnection();
            statement = conn.prepareStatement(sql);
            
            statement.setLong(1, answerId);
            
            rs = statement.executeQuery();
            
            while(rs.next()) {
                long id = rs.getInt("id");
                String value = rs.getString("value");
                boolean isBest = "Y".equals(rs.getString("best"));
                long questionId = rs.getLong("question_id");
                long playerId = rs.getLong("player_id");
                
                Player player = this.getPlayer(playerId);
                
                answer = new Answer(id, value, player);
                answer.setBestAnswer(isBest);
                answer.setQuestionId(questionId);
            }
            
            return answer;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cleanup(conn, statement, rs);
        }
        return answer;
    }
    
    public void acceptAnswer(Question question, Answer answer) {
        Connection conn = null;
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        ResultSet rs = null;

        String sql = "update answer set best = 'Y' where id = ?";
        String sql2 = "update question set isOpen = 'N' where id = ? ";
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            statement = conn.prepareStatement(sql);
            statement.setLong(1, answer.getId());
            statement.execute();

            statement2 = conn.prepareStatement(sql2);
            statement2.setLong(1, question.getId());
            statement2.execute();
            
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            cleanup(conn, statement, rs);
        }
    }
    
    public void upsertQuestion(Question question) {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        String sqlInsert = "INSERT INTO `qingke`.`question` (`value`, `credit`, `player_id`, `isOpen`) VALUES (?,?,?,?)";
        String sqlUpdate = "UPDATE `qingke`.`question` SET `value`=?, `credit`=?, `player_id`=?, `isOpen`=? WHERE `id`=?";
        boolean isUpdate = question.getId() != -1;
        try {
            conn = getConnection();
            
            String sql = sqlInsert;
            if (isUpdate) {
                sql = sqlUpdate;
            }
            statement = conn.prepareStatement(sql);
            
            statement.setString(1, question.getValue());
            statement.setInt(2, question.getCredit());
            statement.setLong(3, question.getFrom().getId());
            statement.setString(4, question.isOpen() ? "Y" : "N");
            if (isUpdate) {
                statement.setLong(5, question.getId());
            }
            
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cleanup(conn, statement, rs);
        }
    }
    
    public void upsertAnwser(Answer answer) {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        String sqlInsert = "INSERT INTO `qingke`.`answer` (`value`, `best`, `player_id`, `question_id`) VALUES (?, ?, ?, ?)";
        String sqlUpdate = "UPDATE `qingke`.`answer` SET `value`=?, `best`=?, `player_id`=?, `question_id`=? WHERE `id`=?";
        boolean isUpdate = answer.getId() != -1;

        try {
            conn = getConnection();

            String sql = sqlInsert;
            if (isUpdate) {
                sql = sqlUpdate;
            }

            statement = conn.prepareStatement(sql);

            statement.setString(1, answer.getValue());
            statement.setString(2, answer.isBestAnswer() ? "Y" : "N");
            statement.setLong(3, answer.getFrom().getId());
            statement.setLong(4, answer.getQuestionId());
            if (isUpdate) {
                statement.setLong(5, answer.getId());
            }

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cleanup(conn, statement, rs);
        }
    }

    // private methods
    private Connection getConnection() throws SQLException{
        com.mysql.jdbc.Driver.class.getName();

        String driver = "com.mysql.jdbc.Driver";
        String url="jdbc:mysql://localhost:3306/qingke?useSSL=false";
        String username = "root";
        String password = "wjp110";

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }

    private void cleanup(Connection conn, Statement statement, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
