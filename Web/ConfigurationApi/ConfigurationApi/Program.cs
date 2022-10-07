using ConfigurationApi.Entities;
using SocketFlow.AspNetCore;
using SocketFlow.Server;
using Vostok.Logging.Abstractions;
using Vostok.Logging.Console;
using Vostok.Logging.File;
using Vostok.Logging.File.Configuration;
using Vostok.Logging.Microsoft;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

var fileLog = new FileLog(new FileLogSettings()
{
    RollingStrategy = new RollingStrategyOptions()
    {
        Period = RollingPeriod.Day,
        MaxFiles = 30,
        Type = RollingStrategyType.ByTime
    }
});

var consoleLog = builder.Environment.IsDevelopment()
    ? (ILog)new SynchronousConsoleLog()
    : new ConsoleLog();

var log = new CompositeLog(consoleLog, fileLog);

builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

builder.Logging.ClearProviders();
builder.Logging.AddVostok(log);

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseCors(c => c
    .WithOrigins("localhost:3000")
    .Build()
);

app.UseWebSockets();

app.UseSocketFlow(string.Empty, flowServer =>
{
    flowServer.OnConnected(c => log.Info($"new client: {c.RemoteEndPoint}"));
    flowServer.Bind<SimpleEntity>(1, (c, v) => log.Info(v.X));
});

app.MapControllers();

app.Lifetime.ApplicationStopped.Register(() =>
{
    log.Info("Flushing after shutdown");
    fileLog.Flush();
});

app.Run();
