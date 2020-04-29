package net.streamarchive.infrastructure.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.streamarchive.infrastructure.enums.StartedBy;
import net.streamarchive.infrastructure.enums.State;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class StatusDataModel {

    @Id
    private Integer vodId;
    private UUID uuid;
    private StartedBy startedBy;
    private Date date;
    private State state;
    private String user;
}
