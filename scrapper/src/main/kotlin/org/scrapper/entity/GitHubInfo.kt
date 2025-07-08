package org.scrapper.entity

import jakarta.persistence.*
import lombok.*
import lombok.experimental.Accessors

@Entity
@Getter
@Setter
@ToString
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "gitHubInfo")
class GitHubInfo() {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gitHubInfo_seq")
    @SequenceGenerator(name = "gitHubInfo_seq", sequenceName = "gitHubInfo_sequence")
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name = "lastCommitCount")
    var lastCommitCount = 0

    @Column(name = "lastBranchCount")
    var lastBranchCount = 0

    @OneToOne
    @JoinColumn(name = "link_id")
    var link: Link? = null

    constructor(id: Long?, lastCommitCount: Int, lastBranchCount: Int, link: Link?) : this() {
        this.id = id
        this.lastCommitCount = lastCommitCount
        this.lastBranchCount = lastBranchCount
        this.link = link
    }
}