
jQuery.extend({
    isHtmlEncoded: function(t) {
        return null != t && (-1 != t.search(/&amp;/g) || -1 != t.search(/&lt;/g) || -1 != t.search(/&gt;/g) || -1 != t.search(/&#39;/g))
    },
    htmlDecode: function(t) {
        return $.isHtmlEncoded(t) ? t.replace(/&amp;/g, "&").replace(/&lt;/g, "<").replace(/&gt;/g, ">").replace(/&#39;/g, "'") : t
    },
    htmlEncode: function(t) {
        return $.isHtmlEncoded(t) ? t : t.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/'/g, "&#39;")
    }
}),
$.extend($.expr[":"], {
    containsi: function(t, e, i) {
        return (t.textContent || t.innerText || "").toLowerCase().indexOf((i[3] || "").toLowerCase()) >= 0
    }
});
var $U = {};
$U.filterStickies = function() {
    var t = $("#search").val();
    t.length > 0 ? ($('div.sticky:not(:containsi("' + t + '"))').hide(),
    $('div.sticky:containsi("' + t + '")').show()) : $("div.sticky").show()
}
,
$U.filterSection = function() {
    var t = $("#retro_section_id");
    if (t.length > 0) {
        var e = t.val();
        e ? ($(".section").hide(),
        $("#section" + e).addClass("filtered full").show()) : ($(".section.filtered").removeClass("full"),
        $(".section").show())
    }
}
,
$U.sortStickies = function() {
    "votes" == $("#sortBy").val() ? $(".section").each(function() {
        $(this).find(".sticky").tsort(".voteCount .count", {
            order: "desc"
        })
    }) : $(".section").each(function() {
        $(this).find(".sticky").tsort({
            attr: "data-id",
            order: "asc"
        })
    })
};

var EditableSticky = function(t) {
    this.sticky = t,
    this.dialog = $("#largeStickyDialog").dialog()
};
EditableSticky.prototype.show = function() {
    var t = this
      , e = this.sticky.element.parents(".section").attr("data-color")
      , i = this.dialog.parent(".ui-widget-content");
    i.removeClass(i.attr("class").split(/\s+/).pop()).addClass(e),
    this.dialog.find(".stickyText").val(this.sticky.decodedText()),
    this.dialog.find(".voteCountContainer .count").html(this.sticky.votes),
    this.dialog.find(".removeStickyButton").unbind("click").click(function() {
        confirm("Do you want to delete this sticky?") && (t.sticky.remove(),
        t.dialog.dialog("close"))
    }),
    this.dialog.find("textarea").unbind("blur").blur(function() {
        t.edit_message($.trim($(this).val()))
    }),
    this.dialog.find(".voteStickyButton").unbind("click").click(function() {
        t.upVote()
    }),
    this.dialog.dialog("option", "beforeClose", function() {
        t.dialog.find("textarea").blur()
    }),
    this.dialog.dialog("open")
}
,
EditableSticky.prototype.edit_message = function(t) {
    var e = this
      , i = this.sticky.text;
    this.sticky.update(t),
    this.dialog.find(".stickyUpdated").text("Updating...").addClass("show"),
    this.sticky.edit({
        message: t,
        oldmessage: i
    }, function() {
        e.dialog.find(".stickyUpdated").text("Updated"),
        setTimeout(function() {
            e.dialog.find(".stickyUpdated").removeClass("show")
        }, 2e3)
    })
}
,
EditableSticky.prototype.upVote = function() {
    var t = this;
    this.dialog.find(".voteUpdated").text("Updating...").addClass("show"),
    this.sticky.edit_vote(this.sticky.votes + 1, function() {
        $(".count", t.dialog).text(t.sticky.votes),
        $(".voteUpdated", t.dialog).text("Updated"),
        setTimeout(function() {
            $(".voteUpdated", t.dialog).removeClass("show")
        }, 2e3)
    })
}
;
var Sticky = function(t) {
    this.element = t,
    this.text = $.trim(t.attr("data-content")),
    this.votes = parseInt($.trim(t.find(".voteCount .count").html())),
    this.id = parseInt(t.attr("data-id"))
};
Sticky.createFrom = function(t, e, i) {
    var n = $.isHtmlEncoded(t) ? t : $.htmlEncode(t)
      , o = $("#stickyTemplate").html()
      , s = $(o);
    return s.hide().attr("title", t).attr("data-content", t).attr("id", "point" + i).attr("data-id", i).find(".stickyText").html(n),
    s.find(".voteCount .count").html(e),
    new Sticky(s)
}
,
Sticky.prototype.decodedText = function() {
    return $.isHtmlEncoded(this.text) ? $.htmlDecode(this.text) : this.text
}
,
Sticky.prototype.encodedText = function() {
    return $.isHtmlEncoded(this.text) ? this.text : $.htmlEncode(this.text)
}
,
Sticky.prototype.displayText = function() {
    return this.encodedText().replace(/\n-{3,}\n/g, "<hr/>")
}
,
Sticky.prototype.titleText = function() {
    return this.text.replace(/\n-{3,}\n/g, " | ")
}
,
Sticky.prototype.sectionId = function() {
    return parseInt(this.element.parents(".section").attr("id").replace("section", ""))
}
,
Sticky.prototype.attachTo = function(t) {
    var e = this;
    $("#section" + t + " .points").append(this.element),
    this.element = $("#section" + t + " .points div.sticky:last"),
    this.element.draggable({
        revert: "invalid",
        scroll: !1,
        containment: ".whiteboard",
        appendTo: "body",
        helper: "clone"
    }).droppable({
        hoverClass: "ui-state-highlight",
        greedy: !0,
        drop: function(t, i) {
            e.merge(new Sticky(i.draggable))
        }
    }).unbind("click").bind("click", function() {
        new EditableSticky(new Sticky(e.element)).show()
    }),
    this.element.show(),
    this.updateDom()
}
,
Sticky.prototype.merge = function(t) {
    if (confirm("You are about to merge two stickies into one. Do you want to continue?")) {
        var e = this
          , i = this.text;
        this.text += "\n---------------\n" + t.text,
        e.updateDom(),
        this.edit({
            message: this.text,
            oldmessage: i
        }, function() {
            t.remove()
        }),
        t.votes > 0 && this.edit_vote(this.votes + t.votes, function() {})
    }
}
,
Sticky.prototype.moveTo = function(t) {
    var e = this;
    this.edit({
        section_id: t.id,
        oldmessage: e.text
    }, function() {
        e.edit_section(t.id, e.text),
        e.removeFromDom(),
        Sticky.createFrom(e.text, e.votes, e.id).attachTo(t.id)
    })
}
,
Sticky.prototype.updateDom = function() {
    this.element.find(".stickyText").html(this.displayText()),
    this.element.attr("data-content", this.text),
    this.element.attr("title", this.titleText()),
    this.element.find(".count").html(this.votes)
}
,
Sticky.prototype.removeFromDom = function() {
    var t = this;
    this.element.hide("slow", function() {
        t.element.remove()
    })
}
,
Sticky.prototype.remove = function() {
    this.removeFromDom(),
    //$a.trackEvent("point", "delete", this.id),
    $.ajax({
        url: "/points/delete/" + this.id,
        type: "GET",
        data: {
            message: this.text
        }
    })
}
,
Sticky.prototype.update = function(t, e) {
    this.text = $.trim(t),
    e && (this.votes = e),
    this.updateDom()
}
,
Sticky.prototype.edit_section = function(t, e) {
    this.edit({
        section_id: t,
        oldmessage: e
    }, function() {})
}
,
Sticky.prototype.edit = function(t, e) {
    $.ajax({
        url: "/points/" + this.id,
        type: "POST",
        data: {
            point: t
        },
        success: function(t) {
            e(t)
        }
    })
}
,
Sticky.prototype.edit_vote = function(t, e) {
    var i = this;
    i.votes = t,
    $.ajax({
        url: "/points/" + i.id + "/votes",
        type: "POST",
        data: {
            vote: {
                point_id: i.id
            }
        },
        success: function(t) {
            i.updateDom(),
            e(t)
        },
        error: function() {
            alert("something went wrong. Please refresh the page")
        }
    })
}
;
var Section = function(t) {
    this.id = t.attr("id").replace("section", ""),
    this.stickies = []
};
Section.prototype.addSticky = function(t) {
    var e = this;
    //$a.trackEvent("point", "create", "section-" + e.id),
    $.ajax({
        url: "/points",
        data: {
            "point[section_id]": e.id,
            "point[message]": t
        },
        type: "POST",
        success: function(t) {
            e.attachSticky(t)
        }
    })
}
,
Section.prototype.attachSticky = function(t) {
    var e = Sticky.createFrom(t.message, t.votes_count, t.id);
    e.attachTo(t.section_id),
    this.stickies.push(e)
}
,
Section.prototype.getDom = function() {
    return $("#section" + this.id)
}
,
Section.prototype.setupEvents = function() {
    var t = this
      , e = function() {
        var e = t.getDom().find(".addStickyForm")
          , i = e.find("textarea");
        e.show("slow"),
        i.focus()
    };
    this.getDom().find(".addStickyButton").click(function() {
        e()
    });
    var i = this.getDom().find(".addStickyForm")
      , n = i.find("textarea");
    n.keypress(function(e) {
        if (13 == e.keyCode) {
            i.hide("slow");
            var n = $(this).val().trim();
            return void o(t.id, n)
        }
        27 == e.keyCode && i.hide("slow")
    }),
    n.blur(function() {
        i.hide("slow");
        var e = $(this).val().trim();
        o(t.id, e)
    });
    var o = function(e, i) {
        i.length > 0 && ($(".stickyText").val(""),
        t.addSticky(i))
    }
}
;
var Ideaboardz = function() {
    var t = this
      , e = new Array;
    this.init = function() {
        i(),
        t.refreshSections()
    }
    ;
    var i = function() {
        $(".section").each(function() {
            var t = $(this)
              , i = new Section(t);
            i.setupEvents(),
            e.push(i)
        })
    }
      , n = function(t) {
        return _.find(e, function(e) {
            return e.id == t
        })
    }
      , o = function(e) {
        var i = [];
        e && a(e);
        for (var o in e) {
            var s = e[o];
            if (l(s))
                r(s);
            else {
                n(s.section_id).attachSticky(s)
            }
            i.push(s)
        }
      //  $U.filterStickies(),
      //  $U.sortStickies(),
        setTimeout(t.refreshSections, 1e4)
    }
      , s = function() {
        return _.map($(".points .sticky"), function(t) {
            return new Sticky($(t))
        })
    }
      , r = function(t) {
        _.find(s(), function(e) {
            return e.id == t.id && e.sectionId() == t.section_id
        }).update(t.message, t.votes_count)
    }
      , a = function(t) {
        var e = _.filter(s(), function(e) {
            return _.find(t, function(t) {
                return e.id == t.id && e.sectionId() == t.section_id
            }) === undefined
        });
        _.invoke(e, "removeFromDom")
    }
      , l = function(t) {
        return $("#section" + t.section_id + " #point" + t.id).length > 0
    };
    this.refreshSections = function() {
        var e = $('meta[name="retroId"]').attr("content")
          , i = $('meta[name="retroName"]').attr("content");
        $.ajax({
            url: "/retros/" +  e + "/points",
            dataType: "json",
            success: o,
            error: function() {
                setTimeout(t.refreshSections, 15e3)
            },
            timeout: 9e3
        })
    }
};
$(document).ready(function() {
    (new Ideaboardz).init(),
    $("#largeStickyDialog").dialog({
        autoOpen: !1,
        height: 255,
        width: 350,
        modal: !0,
        closeOnEscape: !0,
        open: function() {
            $(this).closest(".ui-dialog").find(".ui-dialog-titlebar-close").addClass("stickyClose fas fa-times"),
            $(".ui-widget-overlay").bind("click", function() {
                $("#largeStickyDialog").dialog("close")
            })
        }
    }),
    $("#sortBy").change($U.sortStickies).change(),    
    $("#search").keyup($U.filterStickies),
    $("#search").blur(function() {
        var t = $("#search").val();
        t.length > 0 
    }),
    $("#retro_section_id").change($U.filterSection).change(),
    $(".section").droppable({
        accept: function(t) {
            return $(this).attr("id") != $(t).parents(".section").attr("id")
        },
        hoverClass: "ui-state-highlight",
        drop: function(t, e) {
            var i = new Sticky(e.draggable);
            i.moveTo(new Section($(this)))
        }
    }),
    $("textarea").keyup(function() {
        var t = $(this).attr("maxlength") !== undefined ? parseInt($(this).attr("maxlength")) : 1e8;
        $(this).val().length > t && $(this).value($(this).value().substring(0, t - 1))
    })
});
