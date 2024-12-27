package com.jkpr.chinesecheckers.server.UI;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class ServerWindow extends ApplicationAdapter {
    private Stage stage;
    private Skin skin;
    private Label type;
    private Label number;

    private TextButton submit;

    private SelectBox<String> typeSelect;
    private SelectBox<String> numberSelect;

    @Override
    public void create() {
        // Tworzenie stage i załadowanie skinu
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Window window = new Window("", skin);

        //window options
        window.setSize(500, 500);
        window.setPosition(Gdx.graphics.getWidth() / 2f - window.getWidth() / 2f,
                Gdx.graphics.getHeight() / 2f - window.getHeight() / 2f);

        // Dodanie okna do sceny
        String[] typeOptions = {"Wybierz typ","Option 1", "Option 2", "Option 3"};
        String[] playerOptions = {"wybierz liczbe","Option 1", "Option 2", "Option 3"};

        typeSelect = new SelectBox<>(skin);
        numberSelect = new SelectBox<>(skin);
        submit = new TextButton("submit",skin);

        typeSelect.setItems(typeOptions);
        numberSelect.setItems(playerOptions);

        type = new Label("Wybierz typ gry", skin);
        number = new Label("Wybierz ilość graczy", skin);

        type.setPosition(50, 400);
        typeSelect.setPosition(10, 400);
        number.setPosition(50, 300);
        numberSelect.setPosition(10, 300);
        submit.setPosition(50,100);


        window.addActor(type);
        window.addActor(typeSelect);

        window.addActor(number);
        window.addActor(numberSelect);

        window.addActor(submit);

        stage.addActor(window);

        typeSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String selected = typeSelect.getSelected();
                type.setText(selected);
            }
        });
        numberSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String selected = numberSelect.getSelected();
                number.setText(selected);
            }
        });
        submit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println(typeSelect.getSelected()+" "+numberSelect.getSelected());
            }
        });
    }

    @Override
    public void render() {
        // Czyszczenie ekranu
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Aktualizacja i renderowanie sceny
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
