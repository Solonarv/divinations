# Plans

## General mechanics

Above the clouds, a web of light streams spans the world. Where the streams meet,
there is a node. Each node has a size and is connected to nearby
nodes, forming a Delaunay triangulation.

It is made of a form of energy called Weblight or simply Light. Weblight cannot be lost, created,
or even stored; devices and items are powered by it, but do not consume it.

The web is high in the sky (y=256) and is visible from above cloud height (y>=128).

## Starting out

Travel above the clouds, look up & rightclick a book -> get guide/log-book

Travel up to build limit and look at a node from up close

## Materials

**Light-charged metals/gems**: various metals and gems, infused with Weblight by throwing
into a node.

**Vitreous Weblight:** a block that naturally generates under nodes sometimes.
Fully transparent (like glass), emits light.

## Devices

### Weblight manipulation

Weblight can be drained from and injected into the web, traveling in the form of beams of light.

**Light taps** attach to a node and drain it, sending the Light down to the earth.

**Light spikes** attach to a node and force incoming Light into it.

**Mirrors** reflect a Light beam, changing its direction.

**Prisms** refract and reflect a Light beam, splitting it in two. The ratio depends on the angles.

## Transportation

Transportation uses similarities between nodes, connections, and faces.

Two *nodes* are similar if they have the same number of connections,
to nodes of roughly the same size relative to the central node.

Two *edges* are similar if their endpoints have roughly the same ratio of sizes, and the
same number of further connections.

Two *faces* are similar if they have the same number of nodes, and the node sizes are roughly the same.

### Jumps

Jumps use a connection between two similar *faces* of the web. At least one of the starting point
and destination must have **Jump Pylons** set up, one under each node and connected with a ring of Light.

Upon activation, the user shoots into the sky as a bolt of Light, is transported to the corresponding (by similarity) point
in the destination face, and then falls out of the sky.

### Gates

Gates are permanent portals between two locations based on similar connections. To create a Gate, two
**Gate Pylons** must be suspended under the nodes. Each pylon acts as both a tap and a spike.
These beams of Light, together with the original connection and the bottom of the world, make up the
frame of the Gate. These pylons must be set up at both source and destination.

Any entity that passes through one side of the Gate will come out of the other one at the corresponding location.
If there is no space at the destination, the entity will bounce back.

### Tunnels

A tunnel connects two or more similar nodes. The nodes to connect must have a Tunnel Former attached,
which connects via vertical Light beam to a Tunnel Endpoint. Any items or fluids entering the endpoint will
come out of another connected endpoint.

### Lightriding

Using special equipment it is possible to become tangible enough to Light that one can "ride" the web's streams.
While riding, the player sees the nearby web as well as distant similar nodes, and can travel to any of them.
