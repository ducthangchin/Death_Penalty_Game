import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

public class HangmanBot extends TelegramLongPollingBot {

    public HangmanBot() {
    }

    @Override
    public String getBotUsername() {
        return "hangman_game";
    }

    @Override
    public String getBotToken() {
        return "1645682946:AAEJmi8ZZKidtOYV-Y6--30D3RBgdKBaSdo";
    }

    private final Map<Long, HangmanGame> games = new HashMap<>();
    private final Map<Long, HangmanGame> lastgames = new HashMap<>();

    @Override
    public void onUpdateReceived(Update update) {


//            System.out.println(update.getMessage().getText());
//            System.out.println(update.getMessage().getFrom().getFirstName() );
        SendMessage message = new SendMessage();
        String command = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        System.out.println(command);

        if (command.equals("/clear")){
            lastgames.remove(chatId);
            games.remove(chatId);
        }
        else if(command.equals("/lastgame")){
            if(lastgames.containsKey(chatId)){
                message.setText(lastgames.get(chatId).toString());
            }
            else{
                message.setText("You haven't played a single game yet!");
            }
        }
        else {
            if(!games.containsKey(chatId)){
                if(command.equals("/startgame")){
                    HangmanGame NewGame = new HangmanGame();
                    games.put(chatId,NewGame);
                    message.setText(NewGame.toString());
                }
                else {
                    message.setText("You are not in a game! ");
                }
            }
            else {
                if (command.equals("/startgame")) {
                    String content = "Your game is still running!\n";
                    message.setText(content + games.get(chatId).toString());
                } else if (command.equals("/quitgame")) {
                    String content = "A few will give up instead of holding out to the end..\n¯\\_(ツ)_/¯ ";
                    games.get(chatId).quitGame();
                    message.setText(content);
                    lastgames.put(chatId, games.get(chatId));
                    games.remove(chatId);
                }
                else if (command.equals("/hint")){
                    games.get(chatId).hint();
                    message.setText(games.get(chatId).toString());
                    if (games.get(chatId).gameEnded()){
                        lastgames.put(chatId, games.get(chatId));
                        games.remove(chatId);
                    }
                }
                else {
                    games.get(chatId).execute(command);
                    message.setText(games.get(chatId).toString());
                    if (games.get(chatId).gameEnded()){
                        lastgames.put(chatId, games.get(chatId));
                        games.remove(chatId);
                    }
                }
            }
        }

        message.setChatId(update.getMessage().getChatId().toString());
        try {
            this.execute(message);
        } catch (TelegramApiException e) {
        }


    }

}
