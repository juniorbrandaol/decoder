package com.ead.authuser.models;

import com.ead.authuser.enuns.RoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_ROLES")
public class RoleModel implements GrantedAuthority, Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID roleId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false,unique = true,length = 30)
  private RoleType roleName;

  @Override
  @JsonIgnore
  public String getAuthority() {
    return this.roleName.toString();
  }
}
