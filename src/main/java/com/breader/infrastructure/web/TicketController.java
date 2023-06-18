package com.breader.infrastructure.web;

import com.breader.application.TicketRepository;
import com.breader.application.TicketService;
import com.breader.domain.TicketId;
import com.breader.infrastructure.web.command.CreateTicketCommand;
import com.breader.infrastructure.web.model.TicketDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(TicketController.TICKETS_PATH)
@RequiredArgsConstructor
public class TicketController {

    static final String TICKETS_PATH = "tickets";

    private final TicketService ticketService;
    private final TicketRepository ticketRepository;

    @GetMapping("{id}")
    public ResponseEntity<TicketDto> getTicket(@PathVariable UUID id) {
        return ticketRepository.findById(TicketId.from(id))
                .map(TicketDto::fromTicket)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> createTicket(@RequestBody CreateTicketCommand createTicketCommand) {
        TicketId newTicketId = ticketService.handle(createTicketCommand);
        URI newTicketUri = URI.create("/%s/%s".formatted(TICKETS_PATH, newTicketId.value()));
        return ResponseEntity.created(newTicketUri).build();
    }
}
