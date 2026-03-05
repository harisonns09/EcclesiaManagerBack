package org.ecclesiaManager.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tb_perfil")
public class Perfil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nome; // Ex: "ADMIN", "LIDER", "TESOUREIRO"

    // Fetch EAGER é importante aqui para carregarmos as permissões na hora do login
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tb_perfil_permissao",
            joinColumns = @JoinColumn(name = "perfil_id"),
            inverseJoinColumns = @JoinColumn(name = "permissao_id")
    )
    private Set<Permissao> permissoes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<Permissao> getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(Set<Permissao> permissoes) {
        this.permissoes = permissoes;
    }


}