package com.connection.simpleclient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dto.ActivitateDTO;
import dto.EchipaDTO;
import dto.JocDTO;

/**
 * Created by Alexandru on 9/1/2016.
 */
public class Activitate implements Serializable{
    private ActivitateDTO activitateDTO;
    private JocDTO jocDTO;
    private boolean highPiority = false;
    private boolean isDone = false;
    private boolean isSent = false;
    private int listItem; // un indice optional care arata pozitia activitatii in "activitatiListAdapter"
    private List<EchipaDTO> ECHIPE; //

    public Activitate(ActivitateDTO activitateDTO, JocDTO jocDTO, List<EchipaDTO> echipe) {
        this.activitateDTO = activitateDTO;
        this.jocDTO = jocDTO;
        if (echipe == null)  this.ECHIPE = new ArrayList<>();
        else this.ECHIPE = new ArrayList<>(echipe);
    }

    public boolean addEchipa (EchipaDTO echipa) {
        return ECHIPE.add(echipa);
    }

    public List<EchipaDTO> getECHIPE () {
        return ECHIPE;
    }

    public ActivitateDTO getActivitateDTO() {
        return activitateDTO;
    }

    public void setActivitateDTO(ActivitateDTO activitateDTO) {
        this.activitateDTO = activitateDTO;
    }

    public JocDTO getJocDTO() {
        return jocDTO;
    }

    public void setJocDTO(JocDTO jocDTO) {
        this.jocDTO = jocDTO;
    }

    public int getListItem() {
        return listItem;
    }

    public void setListItem(int listItem) {
        this.listItem = listItem;
    }

    public int getId () {
        if (activitateDTO != null) {
            return activitateDTO.getId();
        } else if (jocDTO != null) {
            return jocDTO.getId();
        }
        return -1;
    }

    public long getIdProgram () {
        if (activitateDTO != null) {
            return activitateDTO.getIdProgram();
        } else if (jocDTO != null) {
            return jocDTO.getIdProgram();
        }
        return -1;
    }

    public void setId (int id) {
        if (activitateDTO != null) {
            activitateDTO.setId(id);
        } else if (jocDTO != null) {
            jocDTO.setId(id);
        }
    }

    public boolean isHighPiority() {
        return highPiority;
    }

    public void setHighPiority(boolean highPiority) {
        if (!isDone) {
            this.highPiority = highPiority;
        }
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        if (!isSent && !isDone){
            isDone = done;
            if (isDone){
                highPiority = false;
            }
        }
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public boolean removeEchipa (EchipaDTO e) {
        if (activitateDTO != null) {
            for (int i = 0; i < activitateDTO.getEchipe().size(); ++i) {
                if (activitateDTO.getEchipe().get(i).getId() == e.getId()) {
                    activitateDTO.getEchipe().remove(i);
                    return true;
                }
            }
            return false;
        } else if (jocDTO != null) {
            for (int i = 0; i < jocDTO.getEchipe().size(); ++i) {
                if (jocDTO.getEchipe().get(i).getId() == e.getId()) {
                    jocDTO.getEchipe().remove(i);
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public void setEchipe (List<EchipaDTO> echipe) {
        if (activitateDTO != null) {
            activitateDTO.setEchipe(echipe);
        } else if (jocDTO != null) {
            jocDTO.setEchipe(echipe);
        }
    }

    public List<EchipaDTO> getEchipe () {
        return activitateDTO != null ?
                activitateDTO.getEchipe() :
                jocDTO.getEchipe();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Activitate)) return false;

        Activitate that = (Activitate) o;

        if (activitateDTO != null ? !activitateDTO.equals(that.activitateDTO) : that.activitateDTO != null)
            return false;
        return jocDTO != null ? jocDTO.equals(that.jocDTO) : that.jocDTO == null;

    }

    @Override
    public int hashCode() {
        int result = activitateDTO != null ? activitateDTO.hashCode() : 0;
        result = 31 * result + (jocDTO != null ? jocDTO.hashCode() : 0);
        return result;
    }
}
