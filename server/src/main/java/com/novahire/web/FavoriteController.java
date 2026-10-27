package com.novahire.web;

import com.novahire.service.FavoriteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public List<Long> list(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return List.of();
        }
        return favoriteService.list(session.getId());
    }

    @PostMapping("/{jobId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void add(@PathVariable Long jobId, HttpSession session) {
        favoriteService.add(session.getId(), jobId);
    }

    @DeleteMapping("/{jobId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long jobId, HttpSession session) {
        favoriteService.remove(session.getId(), jobId);
    }
}
