package com.jkpr.chinesecheckers.client.gui;

import com.jkpr.chinesecheckers.client.boards.AbstractBoardClient;
import com.jkpr.chinesecheckers.client.boards.CellClient;
import com.jkpr.chinesecheckers.server.gamelogic.boards.Position;
import com.jkpr.chinesecheckers.client.Client;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.HashMap;
import java.util.Map;

/**
  * The {@code BoardView} class is a JavaFX component that displays the game board. It is the main component of the GUI.
  */
public class BoardView extends VBox {
    private final AbstractBoardClient board;
    private final Client client;
    private Position firstClick;
    private Circle selectedCircle;
    /**
     * A map that stores the mapping between the position of a cell on the board and the circle that represents it.
     */
    private Map<Position, Circle> circleMap = new HashMap<>();


    public BoardView(AbstractBoardClient board, Client client) {
        this.board = board;
        setSpacing(10);
        createBoardLayout();
        setAlignment(javafx.geometry.Pos.CENTER);
        this.client = client;
        firstClick=null;

    }

    public void update() {
        getChildren().clear();
        createBoardLayout();
    }
    /**
     * Creates the layout of the board by creating a circle for each cell on the board.
     */
    private void createBoardLayout() {
        int maxX=8;
        int maxY=8;
        int minX=-8;
        int minY=-8;

        for (int y=minY; y<=maxY; y++) {
            HBox row = new HBox();
            row.setSpacing(10);
            row.setAlignment(javafx.geometry.Pos.CENTER);
            for (int x=minX; x<=maxX; x++) {
                Position position = new Position(x, y);
                CellClient cell = board.getCell(position);
                if (cell == null) continue;
                Circle circle = createCircle(cell, position);
                circleMap.put(position, circle);
                row.getChildren().add(circle);
            }
            getChildren().add(row);
        }
    }



    /**
      * Creates a circle that represents a cell on the board.
      * @param cell The cell that the circle represents.
      * @param position The position of the cell on the board.
      * @return The circle that represents the cell.
      */
    private Circle createCircle(CellClient cell, Position position) {
        Circle circle = new Circle(20);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(1);
        if (cell.getPiece() ==null){
            circle.setFill(Color.LIGHTGRAY);
        }
        else{
            int owner = cell.getPiece().getOwner();
            circle.setFill(getOwnerColor(owner));
        }
        circle.setOnMouseClicked(event -> {
            handleCellClick(position);
        });
        return circle;
    }

    private Color getOwnerColor(int owner) {
        switch (owner) {
            case 0:
                return Color.BLACK;
            case 1:
                return Color.WHITE;
            case 2:
                return Color.RED;
            case 3:
                return Color.GREEN;
            case 4:
                return Color.BLUE;
            case 5:
                return Color.YELLOW;
            default:
                return Color.VIOLET;
        }
    }
    /**
     * Handles a click on a cell on the board.
     * @param position The position of the cell that was clicked.
     */
    private void handleCellClick(Position position) {
        System.out.println("Clicked on cell: " + position);
        CellClient cell = board.getCell(position);
        if (firstClick==null){
            if (cell.getPiece()!=null&&isMyPiece(cell)){
                firstClick=position;
                System.out.println("First click: "+firstClick.toString());
                highlightCell(position);
            } else {
                System.out.println("Not my piece");
            }
        }
        else{
            Position start = firstClick;
            Position end = position;
            if(start.equals(end)){
                System.out.println("Same cell");
                firstClick=null;
                return;
            }
            unHighlightCell(start);
            sendMoveToServer(start, end);
            firstClick=null;
        }
    }
    private void highlightCell(Position position) {
        Circle circle = circleMap.get(position);
        circle.setStroke(Color.VIOLET);
        circle.setStrokeWidth(1);
        selectedCircle = circle;
    }
    private void unHighlightCell(Position position) {
        Circle circle = circleMap.get(position);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(1);
        selectedCircle = null;
    }

    private boolean isMyPiece(CellClient cell) {
        return cell.getPiece().getOwner() == board.getId();
    }
    /**
     * Sends a move command to the server.
     * @param start The position of the cell that the piece is moving from.
     * @param end The position of the cell that the piece is moving to.
     */
    private void sendMoveToServer(Position start, Position end) {
        String moveCmd = String.format("MOVE %d,%d %d,%d", start.getX(), start.getY(), end.getX(), end.getY());
        System.out.println("Sending move command: " + moveCmd);
        client.sendMessage(moveCmd);
    }
}
