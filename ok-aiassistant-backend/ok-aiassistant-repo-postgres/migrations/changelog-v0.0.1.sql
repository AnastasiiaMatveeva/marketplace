--liquibase formatted sql

--changeset sokatov:1 labels:v0.0.1
CREATE TYPE "model_visibilities_type" AS ENUM ('public', 'owner', 'group');

CREATE TABLE "models" (
	"id" text primary key constraint models_id_length_ctr check (length(id) < 64),
	"title" text constraint models_title_length_ctr check (length(title) < 128),
	"description" text constraint models_description_length_ctr check (length(title) < 4096),
	"solver_path" text CONSTRAINT models_solver_path_length_ctr CHECK (length(solver_path) < 1024),
	"script_path" text CONSTRAINT models_script_path_length_ctr CHECK (length(script_path) < 1024),
	"features" double precision[] DEFAULT '{}',
	"results" double precision[] DEFAULT '{}',
	"visibility" model_visibilities_type not null,
	"owner_id" text not null constraint models_owner_id_length_ctr check (length(id) < 64),
	"lock" text not null constraint models_lock_length_ctr check (length(id) < 64)
);

CREATE INDEX models_owner_id_idx on "models" using hash ("owner_id");

CREATE INDEX models_visibility_idx on "models" using hash ("visibility");
